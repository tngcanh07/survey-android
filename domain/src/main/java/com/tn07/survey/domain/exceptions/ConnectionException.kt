package com.tn07.survey.domain.exceptions

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:01
 */
class ConnectionException(
    cause: Throwable
) : DomainException(cause = cause)