package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.Token
import io.reactivex.rxjava3.core.Observable

/**
 * Created by toannguyen
 * Jul 13, 2021 at 21:28
 */
interface GetTokenUseCase {
    fun getToken(): Token

    fun getTokenObservable(): Observable<Token>
}