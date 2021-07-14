package com.tn07.survey.domain.exceptions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:05
 */
internal class DomainExceptionTest {
    @Test
    fun createException_withoutParams() {
        val exception = DomainException()

        Assertions.assertNull(exception.message)
        Assertions.assertNull(exception.cause)
    }

    @Test
    fun createException_withMessage() {
        val message = "Error-message-at-${System.currentTimeMillis()}"

        val exception = DomainException(message)


        Assertions.assertEquals(message, exception.message)
        Assertions.assertNull(exception.cause)
    }

    @Test
    fun createException_withCause() {
        val cause = RuntimeException()

        val exception = DomainException(cause = cause)

        Assertions.assertNull(exception.message)
        Assertions.assertEquals(cause, exception.cause)
    }

    @Test
    fun createException_withAllParams() {
        val cause = RuntimeException()
        val message = "Error-message-at-${System.currentTimeMillis()}"

        val exception = DomainException(message = message, cause = cause)

        Assertions.assertEquals(message, exception.message)
        Assertions.assertEquals(cause, exception.cause)
    }
}