package com.tn07.survey.data.api.transfomers

import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.exceptions.ConnectionException
import com.tn07.survey.domain.exceptions.DomainException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by toannguyen
 * Jul 23, 2021 at 10:29
 */
class ErrorMapperKtTest {

    @Test
    fun mapToDomainException_connectionException() {
        listOf(
            SocketTimeoutException::class.java,
            IOException::class.java,
            UnknownHostException::class.java,
            NoRouteToHostException::class.java,
            ConnectException::class.java
        ).forEach {
            val exception = Mockito.mock(it)

            val domainException = exception.mapToDomainException() as ConnectionException

            Assert.assertEquals(exception, domainException.cause)
        }
    }

    @Test
    fun mapToDomainException_httpException() {
        val errorCode = 456
        val exception = Mockito.mock(HttpException::class.java)
        Mockito.`when`(exception.code()).thenReturn(errorCode)

        val domainException = exception.mapToDomainException() as ApiException

        Assert.assertEquals(errorCode, domainException.httpCode)
        Assert.assertEquals(exception, domainException.cause)
    }

    @Test
    fun mapToDomainException_alreadyDomainException_doNothing() {
        val exception = Mockito.mock(DomainException::class.java)

        val domainException = exception.mapToDomainException()

        Assert.assertEquals(domainException, exception)
    }

    @Test
    fun mapToDomainException_unknownExceptions_doNothing() {
        val exception = Mockito.mock(Throwable::class.java)

        val domainException = exception.mapToDomainException()

        Assert.assertEquals(domainException, exception)
    }

    @Test
    fun mapToApiException() {
        val statusCode = 477
        val response = Response.error<String>(
            statusCode,
            "".toResponseBody(null)
        )

        val apiException = response.mapToApiException()

        Assert.assertEquals(statusCode, apiException.httpCode)
    }
}