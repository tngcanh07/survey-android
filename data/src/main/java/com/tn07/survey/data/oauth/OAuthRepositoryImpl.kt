package com.tn07.survey.data.oauth

import com.tn07.survey.data.api.transfomers.ApiCompletableTransformer
import com.tn07.survey.data.oauth.datasources.local.OAuthLocalDataSource
import com.tn07.survey.data.oauth.datasources.remote.OAuthRemoteDataSource
import com.tn07.survey.data.oauth.models.AccessTokenDataModel
import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.entities.AnonymousToken
import com.tn07.survey.domain.entities.Token
import com.tn07.survey.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:53
 */
class OAuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: OAuthRemoteDataSource,
    private val localDataSource: OAuthLocalDataSource
) : OAuthRepository {

    private val tokenSubject = BehaviorSubject.createDefault(getToken())

    override fun getToken(): Token {
        return localDataSource.getAccessToken()
            ?.let(AccessTokenDataModel::toAccessToken)
            ?: AnonymousToken
    }

    override fun getTokenObservable(): Observable<Token> {
        return tokenSubject
    }

    override fun login(
        email: String,
        password: String
    ): Single<AccessToken> {
        return remoteDataSource.login(email = email, password = password)
            .doOnSuccess(localDataSource::storeAccessToken)
            .map(AccessTokenDataModel::toAccessToken)
            .doOnSuccess(tokenSubject::onNext)
    }

    override fun refreshToken(accessToken: AccessToken): Single<AccessToken> {
        return remoteDataSource.refreshToken(accessToken.refreshToken)
            .doOnSuccess(localDataSource::storeAccessToken)
            .map(AccessTokenDataModel::toAccessToken)
            .doOnSuccess(tokenSubject::onNext)
    }

    override fun logout(): Completable {
        return Maybe.fromCallable<String> {
            localDataSource.getAccessToken()?.accessToken
        }
            .flatMapCompletable(remoteDataSource::logout)
            .compose(ApiCompletableTransformer())
            .doOnComplete(localDataSource::clearAccessToken)
            .doOnComplete { tokenSubject.onNext(AnonymousToken) }
    }

    override fun requestPassword(email: String): Completable {
        return remoteDataSource.requestPassword(email)
    }
}
