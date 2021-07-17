package com.tn07.survey.domain.repositories

import com.tn07.survey.domain.entities.User
import io.reactivex.rxjava3.core.Observable

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:06
 */
interface UserRepository {
    fun getUserObservable(): Observable<User>
}