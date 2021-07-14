package com.tn07.survey.domain.exceptions

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:02
 */
open class DomainException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)