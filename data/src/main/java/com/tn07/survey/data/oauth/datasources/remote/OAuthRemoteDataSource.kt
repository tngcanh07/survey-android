package com.tn07.survey.data.oauth.datasources.remote

import com.tn07.survey.data.oauth.models.AccessTokenDataModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Created by toannguyen
 * Jul 14, 2021 at 22:55
 */
interface OAuthRemoteDataSource {
    fun login(email: String, password: String): Single<AccessTokenDataModel>

    fun refreshToken(refreshToken: String): Single<AccessTokenDataModel>

    fun logout(accessToken: String): Completable

    fun requestPassword(email: String): Completable
}