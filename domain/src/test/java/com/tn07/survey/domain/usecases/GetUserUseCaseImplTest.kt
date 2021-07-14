package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.AnonymousToken
import com.tn07.survey.domain.entities.Token
import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 13, 2021 at 22:09
 */
internal class GetUserUseCaseImplTest {

    private lateinit var useCase: GetUserUseCaseImpl
    private lateinit var repository: OAuthRepository

    @BeforeEach
    fun setUp() {
        repository = Mockito.mock(OAuthRepository::class.java)
        useCase = GetUserUseCaseImpl(repository)
    }

    @Test
    fun getUser_success_authorizedUser() {
        val expectedToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(repository.getToken()).thenReturn(expectedToken)

        val result = useCase.getUser()

        Assertions.assertEquals(expectedToken, result)
        Mockito.verify(repository).getToken()
    }

    @Test
    fun getUser_success_anonymous() {
        val expectedToken = AnonymousToken
        Mockito.`when`(repository.getToken()).thenReturn(expectedToken)

        val result = useCase.getUser()

        Assertions.assertEquals(expectedToken, result)
        Mockito.verify(repository).getToken()
    }

    @Test
    fun getUserObservable_success() {
        val initToken = AnonymousToken
        val nextToken = Mockito.mock(AccessToken::class.java)
        val tokenSubject = BehaviorSubject.createDefault<Token>(initToken)
        Mockito.`when`(repository.getTokenObservable()).thenReturn(tokenSubject)

        val testObserver = useCase.getUserObservable()
            .test()
            .assertValue(initToken)
            .assertNotComplete()
            .assertNoErrors()

        tokenSubject.onNext(nextToken)

        testObserver
            .assertValueCount(2)
            .assertValueAt(0, initToken)
            .assertValueAt(1, nextToken)
            .assertNoErrors()
            .assertNotComplete()

        Mockito.verify(repository).getTokenObservable()
        Mockito.verifyNoMoreInteractions(repository)
    }

    @Test
    fun getUserObservable_error() {
        val expectedException = IllegalArgumentException()
        Mockito.`when`(repository.getTokenObservable())
            .thenReturn(Observable.error(expectedException))

        useCase.getUserObservable()
            .test()
            .assertError(expectedException)
            .assertNoValues()

        Mockito.verify(repository).getTokenObservable()
        Mockito.verifyNoMoreInteractions(repository)
    }
}