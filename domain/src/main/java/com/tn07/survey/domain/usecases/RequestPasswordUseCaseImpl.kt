package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 13, 2021 at 21:59
 */
class RequestPasswordUseCaseImpl @Inject constructor(
    private val repository: OAuthRepository
) : RequestPasswordUseCase {

    override fun requestPassword(email: String): Completable {
        return repository.requestPassword(email = email)
    }
}