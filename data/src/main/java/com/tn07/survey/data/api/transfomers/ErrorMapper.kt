package com.tn07.survey.data.api.transfomers

import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.exceptions.ConnectionException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:39
 */
internal fun Throwable.mapToDomainException(): Throwable {
    return when (this) {
        is SocketTimeoutException,
        is IOException,
        is UnknownHostException,
        is NoRouteToHostException,
        is ConnectException -> {
            ConnectionException(this)
        }
        is HttpException -> ApiException(httpCode = this.code(), cause = this)
        else -> this
    }
}

internal fun Response<*>.mapToApiException(): ApiException {
    return ApiException(httpCode = code())
}