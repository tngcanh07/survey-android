package com.tn07.survey.domain.usecases

import io.reactivex.rxjava3.core.Completable

/**
 * Created by toannguyen
 * Jul 25, 2021 at 15:16
 */
interface ResetPasswordUseCase {
    
    fun resetPassword(email: String): Completable
}