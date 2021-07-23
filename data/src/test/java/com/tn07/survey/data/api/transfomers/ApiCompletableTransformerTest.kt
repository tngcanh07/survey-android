package com.tn07.survey.data.api.transfomers

import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.exceptions.ConnectionException
import io.reactivex.rxjava3.core.Completable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * Created by toannguyen
 * Jul 23, 2021 at 10:44
 */
class ApiCompletableTransformerTest {

    private lateinit var transformer: ApiCompletableTransformer

    @Before
    fun setUp() {
        transformer = ApiCompletableTransformer()
    }

    @Test
    fun transform_success() {
        Completable.complete()
            .compose(transformer)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun transform_error() {
        val errorCode = 456
        val exception = Mockito.mock(HttpException::class.java)
        Mockito.`when`(exception.code()).thenReturn(errorCode)

        Completable.error(exception)
            .compose(transformer)
            .test()
            .assertError(ApiException::class.java)
            .assertError {
                val apiError = it as ApiException
                Assert.assertEquals(errorCode, apiError.httpCode)
                Assert.assertEquals(exception, apiError.cause)
                true
            }
            .assertNotComplete()
    }


    @Test
    fun transform_error_connectionException() {
        val requestException = Mockito.mock(UnknownHostException::class.java)

        Completable.fromCallable { throw requestException }
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