package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.User
import io.reactivex.rxjava3.core.Observable

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:05
 */
interface GetUserUseCase {
    fun getUserObservable(): Observable<User>
}