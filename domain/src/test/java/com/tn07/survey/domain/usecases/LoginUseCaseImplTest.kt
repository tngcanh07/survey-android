package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

/**
 * Created by toannguyen
 * Jul 13, 2021 at 22:10
 */
internal class LoginUseCaseImplTest {

    private lateinit var useCase: LoginUseCaseImpl
    private lateinit var repository: OAuthRepository

    @BeforeEach
    fun setUp() {
        repository = Mockito.mock(OAuthRepository::class.java)
        useCase = LoginUseCaseImpl(repository)
    }

    @Test
    fun login_success() {
        val email = "mock-email@email.com"
        val password = "mock-password"
        val expectedToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(repository.login(email, password)).thenReturn(Single.just(expectedToken))

        useCase.login(
            email = email,
            password = password
        )
            .test()
            .assertNoErrors()
            .assertComplete()

        Mockito.verify(repository).login(
            email = email,
            password = password
        )
    }

    @Test
    fun login_error() {
        val email = "mock-email@email.com"
        val password = "mock-password"
        val expectedException = IllegalAccessException()
        Mockito.`when`(repository.login(email, password))
            .thenReturn(Single.error(expectedException))

        useCase.login(
            email = email,
            password = password
        )
            .test()
            .assertError(expectedException)
            .assertNotComplete()

        Mockito.verify(repository).login(
            email = email,
            password = password
        )
    }
}