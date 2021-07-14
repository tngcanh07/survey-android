package com.tn07.survey.data.api.transfomers

import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.exceptions.ConnectionException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableSource
import io.reactivex.rxjava3.core.CompletableTransformer
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:29
 */
class ApiCompletableTransformer : CompletableTransformer {
    override fun apply(upstream: Completable): CompletableSource {
        return upstream.onErrorResumeNext {
            Completable.error(it.mapToDomainException())
        }
    }
}
