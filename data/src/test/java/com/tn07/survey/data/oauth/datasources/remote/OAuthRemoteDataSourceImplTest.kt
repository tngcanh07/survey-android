package com.tn07.survey.data.oauth.datasources.remote

import com.google.gson.Gson
import com.tn07.survey.data.api.OAuthConfig
import com.tn07.survey.openResource
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HttpsURLConnection

/**
 * Created by toannguyen
 * Jul 19, 2021 at 18:36
 */
internal class OAuthRemoteDataSourceImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteDataSource: OAuthRemoteDataSourceImpl
    private lateinit var oauthConfig: OAuthConfig

    private val accessTokenJson: String by lazy {
        openResource("access-token.json").use {
            String(it.readBytes())
        }
    }

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
            .build()

        oauthConfig = OAuthConfig(
            baseUrl = "mocked-base-url",
            clientId = "mocked-client-id",
            clientSecret = "mocked-client-secret",
            isLoggingEnabled = true
        )

        remoteDataSource = OAuthRemoteDataSourceImpl(retrofit, oauthConfig)
    }

    @Test
    fun login() {
        val email = "mock@email.com-${System.currentTimeMillis()}"
        val password = "mock-password-${System.currentTimeMillis()}"
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                assertLoginRequestBody(
                    jsonBody = request.body.readString(Charsets.UTF_8),
                    email = email,
                    password = password,
                    oauthConfig = oauthConfig
                )
                return if (
                    request.method == "POST"
                    && request.requestUrl?.pathSegments
                        ?.joinToString("/") == "oauth/token"
                ) {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(accessTokenJson)
                } else {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST)
                }
            }
        }

        remoteDataSource.login(email, password)
            .test()
            .awaitCount(1)
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun refreshToken() {
        val refreshToken = "mock-refresh-token-${System.currentTimeMillis()}"
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                assertRefreshTokenBody(
                    jsonBody = request.body.readString(Charsets.UTF_8),
                    refreshToken = refreshToken,
                    oauthConfig = oauthConfig
                )
                return if (
                    request.method == "POST"
                    && request.requestUrl?.pathSegments
                        ?.joinToString("/") == "oauth/token"
                ) {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(accessTokenJson)
                } else {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST)
                }
            }
        }

        remoteDataSource.refreshToken(refreshToken)
            .test()
            .awaitCount(1)
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun logout() {
        val accessToken = "mock-access-token-${System.currentTimeMillis()}"
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                assertRevokeTokenBody(
                    jsonBody = request.body.readString(Charsets.UTF_8),
                    accessToken = accessToken,
                    oauthConfig = oauthConfig
                )
                return if (
                    request.method == "POST"
                    && request.requestUrl?.pathSegments
                        ?.joinToString("/") == "oauth/revoke"
                ) {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(accessTokenJson)
                } else {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST)
                }
            }
        }

        remoteDataSource.logout(accessToken)
            .test()
            .awaitCount(1)
            .assertNoErrors()
            .assertComplete()
    }

}

private fun assertLoginRequestBody(
    jsonBody: String,
    email: String,
    password: String,
    oauthConfig: OAuthConfig
) {
    val body = Gson().fromJson(jsonBody, LoginRequestBody::class.java)
    Assertions.assertEquals(email, body.email)
    Assertions.assertEquals(password, body.password)
    Assertions.assertEquals(oauthConfig.clientId, body.client_id)
    Assertions.assertEquals(oauthConfig.clientSecret, body.client_secret)
    Assertions.assertEquals("password", body.grant_type)
}

private fun assertRefreshTokenBody(
    jsonBody: String,
    refreshToken: String,
    oauthConfig: OAuthConfig
) {
    val body = Gson().fromJson(jsonBody, RefreshTokenBody::class.java)
    Assertions.assertEquals(refreshToken, body.refresh_token)
    Assertions.assertEquals(oauthConfig.clientId, body.client_id)
    Assertions.assertEquals(oauthConfig.clientSecret, body.client_secret)
    Assertions.assertEquals("refresh_token", body.grant_type)
}

private fun assertRevokeTokenBody(
    jsonBody: String,
    accessToken: String,
    oauthConfig: OAuthConfig
) {
    val body = Gson().fromJson(jsonBody, RevokeTokenBody::class.java)
    Assertions.assertEquals(accessToken, body.token)
    Assertions.assertEquals(oauthConfig.clientId, body.client_id)
    Assertions.assertEquals(oauthConfig.clientSecret, body.client_secret)
}

private class LoginRequestBody(
    val email: String,
    val password: String,
    val client_id: String,
    val client_secret: String,
    val grant_type: String,
)

private class RefreshTokenBody(
    val refresh_token: String,
    val client_id: String,
    val client_secret: String,
    val grant_type: String,
)

private class RevokeTokenBody(
    val token: String,
    val client_id: String,
    val client_secret: String,
)
