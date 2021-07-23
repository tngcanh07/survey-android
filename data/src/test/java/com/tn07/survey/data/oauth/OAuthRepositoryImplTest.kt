package com.tn07.survey.data.oauth

import com.tn07.survey.data.oauth.datasources.local.OAuthLocalDataSource
import com.tn07.survey.data.oauth.datasources.remote.OAuthRemoteDataSource
import com.tn07.survey.data.oauth.models.AccessTokenDataModel
import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.AnonymousToken
import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.exceptions.ConnectionException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.net.ConnectException
import java.net.SocketTimeoutException

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
    fun getToken_anonymous() {
        Assert.assertEquals(AnonymousToken, repository.getToken())
    }

    @Test
    fun getToken() {
        val accessTokenDataModel = AccessTokenDataModel(
            tokenType = "token-type",
            accessToken = "access-token-${System.currentTimeMillis()}",
            refreshToken = "refresh-token-${System.currentTimeMillis()}",
            createdAt = System.currentTimeMillis() / 1000,
            expiresIn = 100000
        )
        Mockito.`when`(localDataSource.getAccessToken()).thenReturn(accessTokenDataModel)

        val accessToken = repository.getToken() as AccessToken

        Assert.assertEquals(accessTokenDataModel.accessToken, accessToken.accessToken)
        Assert.assertEquals(accessTokenDataModel.refreshToken, accessToken.refreshToken)
        Assert.assertEquals(accessTokenDataModel.tokenType, accessToken.tokenType)
        Assert.assertEquals(accessTokenDataModel.expiresIn, accessToken.expiresIn)
        Assert.assertEquals(accessTokenDataModel.createdAt, accessToken.createdAt)
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

        repository.getTokenObservable()
            .test()
            .assertValue(accessToken)

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
    fun refreshToken_error_connectionException() {
        val refreshToken = "mock-refresh-token"
        val currentToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(currentToken.refreshToken).thenReturn(refreshToken)
        val expectedError = ConnectionException(SocketTimeoutException())

        Mockito.`when`(remoteDataSource.refreshToken(refreshToken))
            .thenReturn(Single.error(expectedError))

        repository.refreshToken(currentToken)
            .test()
            .assertError(expectedError)

        Mockito.verify(remoteDataSource).refreshToken(refreshToken)
        Mockito.verify(localDataSource, Mockito.never()).clearAccessToken()
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

        repository.getTokenObservable()
            .test()
            .assertValue(AnonymousToken)

        Mockito.verify(remoteDataSource).logout(token)
        Mockito.verify(localDataSource).clearAccessToken()
    }

    @Test
    fun logout_alreadyLoggedOut() {
        Mockito.`when`(localDataSource.getAccessToken()).thenReturn(null)

        repository.logout()
            .test()
            .assertNoErrors()
            .assertComplete()

        Mockito.verifyNoInteractions(remoteDataSource)
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

    @Test
    fun requestPassword() {
        val email = "mock@email.com"

        Mockito.`when`(remoteDataSource.requestPassword(email))
            .thenReturn(Completable.complete())

        repository.requestPassword(email)
            .test()
            .assertNoErrors()
            .assertComplete()

        Mockito.verify(remoteDataSource).requestPassword(email)
    }

    @Test
    fun requestPassword_error() {
        val email = "mock@email.com"
        val expectedException = ApiException(123)
        Mockito.`when`(remoteDataSource.requestPassword(email))
            .thenReturn(Completable.error(expectedException))

        repository.requestPassword(email)
            .test()
            .assertError(expectedException)

        Mockito.verify(remoteDataSource).requestPassword(email)
    }
}