package com.tn07.survey.data.api

import com.tn07.survey.data.api.mock.ApiService
import com.tn07.survey.data.api.mock.mockAccessToken
import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.AnonymousToken
import com.tn07.survey.domain.entities.authorizationHeader
import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import javax.net.ssl.HttpsURLConnection

/**
 * Created by toannguyen
 * Jul 23, 2021 at 11:44
 */
class ApiRequestAuthenticatorTest {

    private lateinit var authenticator: ApiRequestAuthenticator
    private lateinit var oauthRepository: OAuthRepository
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService
    private val validAccessToken = mockAccessToken(
        tokenType = "token-type",
        accessToken = "access-token-${System.currentTimeMillis()}",
        refreshToken = "refresh-token-${System.currentTimeMillis()}",
        createdAt = System.currentTimeMillis() / 1000,
        expiresIn = 100000
    )

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val authorization = request.headers["Authorization"]
                return if (authorization == validAccessToken.authorizationHeader) {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody("""{ "data": "$authorization" }""")
                } else {
                    MockResponse().setResponseCode(HttpsURLConnection.HTTP_UNAUTHORIZED)
                }
            }
        }
        oauthRepository = Mockito.mock(OAuthRepository::class.java)
        authenticator = ApiRequestAuthenticator(repository = oauthRepository)

        val okHttpClient = OkHttpClient.Builder()
            .authenticator(authenticator)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Test
    fun authenticate_success() {
        val accessToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(oauthRepository.getToken()).thenReturn(accessToken)
        Mockito.`when`(oauthRepository.refreshToken(accessToken))
            .thenReturn(Single.just(validAccessToken))

        apiService.getUserInfo()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()

        Mockito.verify(oauthRepository).getToken()
        Mockito.verify(oauthRepository).refreshToken(accessToken)
    }

    @Test
    fun authenticate_error_refreshTokenFailed() {
        val accessToken = Mockito.mock(AccessToken::class.java)
        Mockito.`when`(oauthRepository.getToken()).thenReturn(accessToken)
        Mockito.`when`(oauthRepository.refreshToken(accessToken))
            .thenReturn(Single.error(SocketTimeoutException()))

        apiService.getUserInfo()
            .test()
            .await()
            .assertError(HttpException::class.java)

        Mockito.verify(oauthRepository).getToken()
        Mockito.verify(oauthRepository).refreshToken(accessToken)
    }

    @Test
    fun authenticate_error_refreshReturnInvalidToken() {
        val accessToken = Mockito.mock(AccessToken::class.java)
        val refreshedToken = mockAccessToken(
            createdAt = 0L,
            expiresIn = 100000
        )
        Mockito.`when`(oauthRepository.getToken()).thenReturn(accessToken)
        Mockito.`when`(oauthRepository.refreshToken(accessToken))
            .thenReturn(Single.just(refreshedToken))

        apiService.getUserInfo()
            .test()
            .await()
            .assertError(HttpException::class.java)

        Mockito.verify(oauthRepository).getToken()
        Mockito.verify(oauthRepository).refreshToken(accessToken)
    }

    @Test
    fun authenticate_error_anonymous() {
        Mockito.`when`(oauthRepository.getToken()).thenReturn(AnonymousToken)

        apiService.getUserInfo()
            .test()
            .await()
            .assertError(HttpException::class.java)

        Mockito.verify(oauthRepository).getToken()
    }
}