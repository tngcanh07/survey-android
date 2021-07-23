package com.tn07.survey.data.oauth

import com.tn07.survey.data.oauth.datasources.local.OAuthLocalDataSource
import com.tn07.survey.data.oauth.datasources.remote.OAuthRemoteDataSource
import com.tn07.survey.data.oauth.models.AccessTokenDataModel
import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.exceptions.ConnectionException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.net.ConnectException

/**
 * Created by toannguyen
 * Jul 19, 2021 at 20:22
 */
internal class OAuthRepositoryImplTest {
    private lateinit var repository: OAuthRepositoryImpl
    private lateinit var remoteDataSource: OAuthRemoteDataSource
    private lateinit var localDataSource: OAuthLocalDataSource


    @Before
    fun setUp() {
        remoteDataSource = Mockito.mock(OAuthRemoteDataSource::class.java)
        localDataSource = Mockito.mock(OAuthLocalDataSource::class.java)
        repository = OAuthRepositoryImpl(remoteDataSource, localDataSource)
    }


    @Test
    fun login() {
        val email = "mock-email@host.com"
        val password = "mock-password"
        val accessToken = Mockito.mock(AccessTokenDataModel::class.java)
        Mockito.`when`(remoteDataSource.login(email, password))
            .thenReturn(Single.just(accessToken))

        repository.login(email, password)
            .test()
            .assertValue(accessToken)
            .assertNoErrors()
            .assertComplete()

        Mockito.verify(remoteDataSource).login(email, password)
        Mockito.verify(localDataSource).storeAccessToken(accessToken)
    }

    @Test
    fun login_error() {
        val email = "mock-email@host.com"
        val password = "mock-password"
        val accessToken = Mockito.mock(AccessTokenDataModel::class.java)
        Mockito.`when`(remoteDataSource.login(email, password))
            .thenReturn(Single.error(ConnectionException(ConnectException())))

        repository.login(email, password)
            .test()
            .assertError(ConnectionException::class.java)

        Mockito.verify(remoteDataSource).login(email, password)
        Mockito.verify(localDataSource, Mockito.never())
            .storeAccessToken(Mockito.any() ?: accessToken)
    }

    @Test
    fun refreshToken() {
        val refreshToken = "mock-refresh-token"
        val currentToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(currentToken.refreshToken).thenReturn(refreshToken)
        val accessToken = Mockito.mock(AccessTokenDataModel::class.java)
        Mockito.`when`(remoteDataSource.refreshToken(refreshToken))
            .thenReturn(Single.just(accessToken))

        repository.refreshToken(currentToken)
            .test()
            .assertValue(accessToken)
            .assertNoErrors()
            .assertComplete()

        Mockito.verify(remoteDataSource).refreshToken(refreshToken)
        Mockito.verify(localDataSource).storeAccessToken(accessToken)
    }

    @Test
    fun refreshToken_error_400_clearAccessToken() {
        val refreshToken = "mock-refresh-token"
        val currentToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(currentToken.refreshToken).thenReturn(refreshToken)
        val expectedError = ApiException(400)

        Mockito.`when`(remoteDataSource.refreshToken(refreshToken))
            .thenReturn(Single.error(expectedError))

        repository.refreshToken(currentToken)
            .test()
            .assertError(expectedError)

        Mockito.verify(remoteDataSource).refreshToken(refreshToken)
        Mockito.verify(localDataSource).clearAccessToken()
    }

    @Test
    fun logout() {
        val token = "mock-access-token"
        val currentToken = Mockito.mock(AccessTokenDataModel::class.java)
        Mockito.`when`(currentToken.accessToken).thenReturn(token)

        Mockito.`when`(localDataSource.getAccessToken()).thenReturn(currentToken)
        Mockito.`when`(remoteDataSource.logout(token))
            .thenReturn(Completable.complete())

        repository.logout()
            .test()
            .assertNoErrors()
            .assertComplete()

        Mockito.verify(remoteDataSource).logout(token)
        Mockito.verify(localDataSource).clearAccessToken()
    }

    @Test
    fun logout_error() {
        val token = "mock-access-token"
        val currentToken = Mockito.mock(AccessTokenDataModel::class.java)
        Mockito.`when`(currentToken.accessToken).thenReturn(token)

        Mockito.`when`(localDataSource.getAccessToken()).thenReturn(currentToken)
        Mockito.`when`(remoteDataSource.logout(token))
            .thenReturn(Completable.error(ConnectException()))

        repository.logout()
            .test()
            .assertError(ConnectionException::class.java)

        Mockito.verify(remoteDataSource).logout(token)
        Mockito.verify(localDataSource, Mockito.never()).clearAccessToken()
    }
}