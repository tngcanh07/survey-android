package com.tn07.survey.domain.usecases

import io.reactivex.rxjava3.core.Completable

/**
 * Created by toannguyen
 * Jul 13, 2021 at 21:33
 */
interface LogoutUseCase {
    fun logout(): Completable
}