package com.tn07.survey.domain.usecases

import io.reactivex.rxjava3.core.Completable

/**
 * Created by toannguyen
 * Jul 13, 2021 at 21:05
 */
interface LoginUseCase {
    fun login(
        email: String,
        password: String
    ): Completable
}