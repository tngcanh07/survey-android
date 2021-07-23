package com.tn07.survey.data.api.transfomers

import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.exceptions.ConnectionException
import com.tn07.survey.domain.exceptions.DomainException
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response
import java.net.UnknownHostException

/**
 * Created by toannguyen
 * Jul 23, 2021 at 10:21
 */
class ApiSingleTransformerTest {

    private lateinit var transformer: ApiSingleTransformer<String>

    @Before
    fun setUp() {
        transformer = ApiSingleTransformer()
    }

    @Test
    fun transform_success() {
        val message = "message-${System.currentTimeMillis()}"
        val response = Response.success(message)

        Single.fromCallable<Response<String>> { response }
            .compose(transformer)
            .test()
            .assertComplete()
            .assertValue(message)
    }

    @Test
    fun transform_error_noResponseBody() {
        val response = Response.success<String>(null)

        Single.fromCallable<Response<String>> { response }
            .compose(transformer)
            .test()
            .assertError(DomainException::class.java)
            .assertError {
                val exception = it as DomainException
                Assert.assertEquals("NO_RESPONSE_BODY", exception.message)
                true
            }
    }

    @Test
    fun transform_error_requestUnsuccessful() {
        val statusCode = 477

        val response = Response.error<String>(
            statusCode,
            "".toResponseBody(null)
        )

        Single.fromCallable<Response<String>> { response }
            .compose(transformer)
            .test()
            .assertError(ApiException::class.java)
            .assertError {
                val exception = it as ApiException
                Assert.assertEquals(statusCode, exception.httpCode)
                Assert.assertNull(exception.cause)
                true
            }
    }

    @Test
    fun transform_error_connectionException() {
        val requestException = Mockito.mock(UnknownHostException::class.java)

        Single.fromCallable<Response<String>> { throw requestException }
            .compose(transformer)
            .test()
            .assertError(ConnectionException::class.java)
            .assertError {
                val exception = it as ConnectionException
                Assert.assertEquals(requestException, exception.cause)
                true
            }
            .assertNotComplete()
    }
}