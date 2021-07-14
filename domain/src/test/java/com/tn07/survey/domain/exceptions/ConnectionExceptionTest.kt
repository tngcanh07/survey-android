package com.tn07.survey.domain.exceptions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.UnknownHostException

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:06
 */
internal class ConnectionExceptionTest {
    @Test
    fun createException() {
        val cause = UnknownHostException()
        val exception = ConnectionException(cause)

        Assertions.assertNull(exception.message)
        Assertions.assertEquals(cause, exception.cause)
    }
}