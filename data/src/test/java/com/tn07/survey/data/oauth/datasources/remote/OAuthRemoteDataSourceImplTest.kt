package com.tn07.survey.data.oauth.datasources.remote

import com.google.gson.Gson
import com.tn07.survey.data.TestDataProvider
import com.tn07.survey.data.api.OAuthConfig
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
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

    @Before
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
                        .setBody(TestDataProvider.accessTokenJson)
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
                        .setBody(TestDataProvider.accessTokenJson)
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
                        .setBody(TestDataProvider.accessTokenJson)
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

    @Test
    fun requestPassword() {
        val email = "mock-email@mail.com-${System.currentTimeMillis()}"
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                assertRequestPasswordBody(
                    jsonBody = request.body.readString(Charsets.UTF_8),
                    email = email,
                    oauthConfig = oauthConfig
                )
                return if (
                    request.method == "POST"
                    && request.requestUrl?.pathSegments
                        ?.joinToString("/") == "passwords"
                ) {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(TestDataProvider.accessTokenJson)
                } else {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST)
                }
            }
        }

        remoteDataSource.requestPassword(email)
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
    Assert.assertEquals(email, body.email)
    Assert.assertEquals(password, body.password)
    Assert.assertEquals(oauthConfig.clientId, body.client_id)
    Assert.assertEquals(oauthConfig.clientSecret, body.client_secret)
    Assert.assertEquals("password", body.grant_type)
}

private fun assertRefreshTokenBody(
    jsonBody: String,
    refreshToken: String,
    oauthConfig: OAuthConfig
) {
    val body = Gson().fromJson(jsonBody, RefreshTokenBody::class.java)
    Assert.assertEquals(refreshToken, body.refresh_token)
    Assert.assertEquals(oauthConfig.clientId, body.client_id)
    Assert.assertEquals(oauthConfig.clientSecret, body.client_secret)
    Assert.assertEquals("refresh_token", body.grant_type)
}

private fun assertRevokeTokenBody(
    jsonBody: String,
    accessToken: String,
    oauthConfig: OAuthConfig
) {
    val body = Gson().fromJson(jsonBody, RevokeTokenBody::class.java)
    Assert.assertEquals(accessToken, body.token)
    Assert.assertEquals(oauthConfig.clientId, body.client_id)
    Assert.assertEquals(oauthConfig.clientSecret, body.client_secret)
}

private fun assertRequestPasswordBody(
    jsonBody: String,
    email: String,
    oauthConfig: OAuthConfig
) {
    val body = Gson().fromJson(jsonBody, RequestPasswordBody::class.java)
    Assert.assertEquals(email, body.user.email)
    Assert.assertEquals(oauthConfig.clientId, body.client_id)
    Assert.assertEquals(oauthConfig.clientSecret, body.client_secret)
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

private class RequestPasswordBody(
    val client_id: String,
    val client_secret: String,
    val user: RequestPasswordUser
)

private class RequestPasswordUser(
    val email: String
)
