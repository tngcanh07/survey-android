package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Completable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 13, 2021 at 22:10
 */
internal class LogoutUseCaseImplTest {

    private lateinit var useCase: LogoutUseCaseImpl
    private lateinit var repository: OAuthRepository

    @BeforeEach
    fun setUp() {
        repository = Mockito.mock(OAuthRepository::class.java)
        useCase = LogoutUseCaseImpl(repository)
    }

    @Test
    fun logout_success() {
        Mockito.`when`(repository.logout()).thenReturn(Completable.complete())

        useCase.logout()
            .test()
            .assertNoErrors()
            .assertComplete()

        Mockito.verify(repository).logout()
    }

    @Test
    fun logout_error() {
        val expectedException = IllegalArgumentException()
        Mockito.`when`(repository.logout()).thenReturn(Completable.error(expectedException))

        useCase.logout()
            .test()
            .assertError(expectedException)
            .assertNotComplete()

        Mockito.verify(repository).logout()
    }
}