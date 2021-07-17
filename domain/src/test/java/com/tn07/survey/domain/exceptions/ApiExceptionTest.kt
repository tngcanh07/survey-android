package com.tn07.survey.domain.exceptions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:07
 */
internal class ApiExceptionTest {
    @Test
    fun createException_defaultParams() {
        val httpCode = 423
        val exception = ApiException(httpCode)

        Assertions.assertNull(exception.cause)
        Assertions.assertEquals("code=$httpCode null", exception.message)
        Assertions.assertEquals(httpCode, exception.httpCode)
    }

    @Test
    fun createException_withoutCause() {
        val httpCode = 423
        val exception = ApiException(httpCode, null)

        Assertions.assertNull(exception.cause)
        Assertions.assertEquals("code=$httpCode null", exception.message)
        Assertions.assertEquals(httpCode, exception.httpCode)
    }

    @Test
    fun createException_withAllParams() {
        val httpCode = 423
        val cause = Exception("error-message")

        val exception = ApiException(httpCode, cause)

        Assertions.assertEquals(cause, exception.cause)
        Assertions.assertEquals("code=$httpCode error-message", exception.message)
        Assertions.assertEquals(httpCode, exception.httpCode)
    }
}