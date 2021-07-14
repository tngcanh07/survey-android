package com.tn07.survey.domain.repositories

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.Token
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Created by toannguyen
 * Jul 13, 2021 at 17:39
 */
interface OAuthRepository {
    fun getToken(): Token

    fun getTokenObservable(): Observable<Token>

    fun login(email: String, password: String): Single<AccessToken>

    fun refreshToken(accessToken: AccessToken): Single<AccessToken>

    fun logout(): Completable

    fun requestPassword(email: String): Completable
}