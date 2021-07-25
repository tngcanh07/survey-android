package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 25, 2021 at 15:17
 */
class ResetPasswordUseCaseImpl @Inject constructor(
    private val repository: OAuthRepository
) : ResetPasswordUseCase {

    override fun resetPassword(email: String): Completable {
        return repository.requestPassword(email)
    }
}