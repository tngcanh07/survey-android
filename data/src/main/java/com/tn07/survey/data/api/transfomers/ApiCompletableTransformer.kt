package com.tn07.survey.data.api.transfomers

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableSource
import io.reactivex.rxjava3.core.CompletableTransformer

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
