package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.Token
import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 13, 2021 at 21:29
 */
class GetUserUseCaseImpl @Inject constructor(
    private val repository: OAuthRepository
) : GetUserUseCase {

    override fun getUser(): Token {
        return repository.getToken()
    }

    override fun getUserObservable(): Observable<Token> {
        return repository.getTokenObservable()
    }
}