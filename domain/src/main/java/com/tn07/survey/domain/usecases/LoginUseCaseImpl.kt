package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 13, 2021 at 21:05
 */
class LoginUseCaseImpl @Inject constructor(
    private val repository: OAuthRepository
) : LoginUseCase {

    override fun login(
        email: String,
        password: String
    ): Completable {
        return repository.login(
            email = email,
            password = password
        )
            .ignoreElement()
    }
}