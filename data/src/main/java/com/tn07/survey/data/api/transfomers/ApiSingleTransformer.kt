package com.tn07.survey.data.api.transfomers

import com.tn07.survey.domain.exceptions.DomainException
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.core.SingleTransformer
import retrofit2.Response

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:26
 */
class ApiSingleTransformer<T> : SingleTransformer<Response<T>, T> {
    override fun apply(upstream: Single<Response<T>>): SingleSource<T> {
        return upstream
            .map { response: Response<T> ->
                if (response.isSuccessful) {
                    response.body() ?: throw DomainException("NO_RESPONSE_BODY")
                } else {
                    throw response.mapToApiException()
                }
            }
            .onErrorResumeNext { throwable: Throwable ->
                Single.error(throwable.mapToDomainException())
            }
    }
}
