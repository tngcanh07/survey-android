package com.tn07.survey.data.user.datasources.remote

import com.tn07.survey.data.TestDataProvider
import com.tn07.survey.domain.exceptions.ApiException
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
 * Jul 23, 2021 at 14:31
 */
class UserRemoteDataSourceImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var dataSource: UserRemoteDataSourceImpl

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
            .build()
        dataSource = UserRemoteDataSourceImpl(retrofit)
    }

    @Test
    fun getUser() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return if (
                    request.method == "GET"
                    && request.requestUrl?.pathSegments
                        ?.joinToString("/") == "me"
                ) {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(TestDataProvider.userJson)
                } else {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST)
                }
            }
        }

        dataSource.getUser()
            .test()
            .await()
            .assertNoErrors()
            .assertValue {
                Assert.assertEquals("hoang.mirs@gmail.com", it.email)
                Assert.assertEquals(
                    "https://api.adorable.io/avatar/hoang.mirs@gmail.com",
                    it.avatarUrl
                )
                return@assertValue true
            }
            .assertComplete()
    }

    @Test
    fun getUser_error() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST)
            }
        }

        dataSource.getUser()
            .test()
            .await()
            .assertError(ApiException::class.java)
            .assertError {
                val apiException = it as ApiException
                Assert.assertEquals(HttpsURLConnection.HTTP_BAD_REQUEST, apiException.httpCode)
                return@assertError true
            }
    }
}