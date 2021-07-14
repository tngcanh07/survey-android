package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Completable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 13, 2021 at 22:20
 */
internal class RequestPasswordUseCaseImplTest {

    private lateinit var useCase: RequestPasswordUseCaseImpl
    private lateinit var repository: OAuthRepository

    @BeforeEach
    fun setUp() {
        repository = Mockito.mock(OAuthRepository::class.java)
        useCase = RequestPasswordUseCaseImpl(repository)
    }

    @Test
    fun requestPassword_success() {
        val email = "mock-email@email.com"
        Mockito.`when`(repository.requestPassword(email)).thenReturn(Completable.complete())

        useCase.requestPassword(email = email)
            .test()
            .assertComplete()
            .assertNoErrors()

        Mockito.verify(repository).requestPassword(email = email)
    }

    @Test
    fun requestPassword_error() {
        val email = "mock-email@email.com"
        val expectedException = IllegalArgumentException()
        Mockito.`when`(repository.requestPassword(email))
            .thenReturn(Completable.error(expectedException))

        useCase.requestPassword(email = email)
            .test()
            .assertError(expectedException)
            .assertNotComplete()

        Mockito.verify(repository).requestPassword(email = email)
    }
}