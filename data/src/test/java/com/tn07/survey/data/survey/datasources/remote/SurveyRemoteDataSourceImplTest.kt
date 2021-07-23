package com.tn07.survey.data.survey.datasources.remote

import com.tn07.survey.data.TestDataProvider
import com.tn07.survey.data.api.PageableApiResponse
import com.tn07.survey.data.survey.model.SurveyResponse
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
 * Jul 23, 2021 at 13:44
 */
class SurveyRemoteDataSourceImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteDataSource: SurveyRemoteDataSourceImpl

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
            .build()

        remoteDataSource = SurveyRemoteDataSourceImpl(retrofit)
    }

    @Test
    fun getSurveys() {
        val page = 1
        val pageSize = 2
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return if (
                    request.method == "GET"
                    && request.requestUrl?.pathSegments
                        ?.joinToString("/") == "surveys"
                ) {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_OK)
                        .setBody(TestDataProvider.surveysJson)
                } else {
                    MockResponse()
                        .setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST)
                }
            }
        }

        remoteDataSource.getSurveys(page, pageSize)
            .test()
            .await()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(::assertSurveysResponse)
            .assertComplete()
    }

    @Test
    fun getSurveys_error() {
        val page = 1
        val pageSize = 2
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST)
            }
        }

        remoteDataSource.getSurveys(page, pageSize)
            .test()
            .await()
            .assertError(ApiException::class.java)
            .assertError {
                val apiException = it as ApiException
                Assert.assertEquals(HttpsURLConnection.HTTP_BAD_REQUEST, apiException.httpCode)
                return@assertError true
            }
    }

    private fun assertSurveysResponse(surveys: PageableApiResponse<SurveyResponse>): Boolean {
        val metaData = surveys.meta
        Assert.assertEquals(1, metaData.page)
        Assert.assertEquals(2, metaData.pageSize)
        Assert.assertEquals(10, metaData.pages)
        Assert.assertEquals(20, metaData.records)

        // data
        val items = surveys.data
        Assert.assertEquals(2, items.size)
        items.forEach {
            Assert.assertNotNull(it.id)
            Assert.assertEquals("survey", it.type)

            Assert.assertNotNull(it.attributes.coverImageUrl)
            Assert.assertNotNull(it.attributes.description)
            Assert.assertNotNull(it.attributes.title)
        }
        return true
    }
}