package com.tn07.survey.data.user

import com.tn07.survey.data.user.datasources.remote.UserRemoteDataSource
import com.tn07.survey.domain.entities.User
import com.tn07.survey.domain.repositories.UserRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:11
 */
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override fun getUserObservable(): Observable<User> {
        return remoteDataSource.getUser()
            .map<User> { it }
            .toObservable()
    }
}