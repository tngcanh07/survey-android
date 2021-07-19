package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.User
import com.tn07.survey.domain.repositories.UserRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:05
 */
class GetUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : GetUserUseCase {

    override fun getUserObservable(): Observable<User> {
        return repository.getUserObservable()
    }
}