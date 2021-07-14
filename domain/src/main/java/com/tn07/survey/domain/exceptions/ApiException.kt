package com.tn07.survey.domain.exceptions

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:02
 */
class ApiException(
    val httpCode: Int,
    cause: Throwable? = null
) : DomainException(cause = cause)