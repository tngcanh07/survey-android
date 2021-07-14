package com.tn07.survey.data.oauth.datasources.remote

import com.tn07.survey.data.api.transfomers.ApiCompletableTransformer
import com.tn07.survey.data.api.transfomers.ApiSingleTransformer
import com.tn07.survey.data.api.OAuthConfig
import com.tn07.survey.data.oauth.datasources.remote.requestmodel.RefreshTokenRequestModel
import com.tn07.survey.data.oauth.datasources.remote.requestmodel.RequestPasswordRequestModel
import com.tn07.survey.data.oauth.datasources.remote.requestmodel.RequestTokenRequestModel
import com.tn07.survey.data.oauth.datasources.remote.requestmodel.RevokeTokenRequestModel
import com.tn07.survey.data.oauth.models.AccessTokenDataModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:09
 */
class OAuthRemoteDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit,
    private val oauthConfig: OAuthConfig
) : OAuthRemoteDataSource {
    private val oauthApi: OAuthApi by lazy {
        retrofit.create(OAuthApi::class.java)
    }

    override fun login(email: String, password: String): Single<AccessTokenDataModel> {
        val requestModel = RequestTokenRequestModel(
            email = email,
            password = password,
            clientId = oauthConfig.clientId,
            clientSecret = oauthConfig.clientSecret
        )
        return oauthApi.requestToken(requestModel)
            .compose(ApiSingleTransformer())
            .map { it.data.accessToken }
    }

    override fun refreshToken(refreshToken: String): Single<AccessTokenDataModel> {
        val requestModel = RefreshTokenRequestModel(
            refreshToken = refreshToken,
            clientId = oauthConfig.clientId,
            clientSecret = oauthConfig.clientSecret
        )
        return oauthApi.refreshToken(requestModel)
            .compose(ApiSingleTransformer())
            .map { it.data.accessToken }
    }

    override fun logout(accessToken: String): Completable {
        val requestModel = RevokeTokenRequestModel(
            token = accessToken,
            clientId = oauthConfig.clientId,
            clientSecret = oauthConfig.clientSecret
        )
        return oauthApi.revokeToken(requestModel)
    }

    override fun requestPassword(email: String): Completable {
        val requestModel = RequestPasswordRequestModel(
            user = RequestPasswordRequestModel.User(
                email = email
            ),
            clientId = oauthConfig.clientId,
            clientSecret = oauthConfig.clientSecret
        )
        return oauthApi.requestPassword(requestModel)
            .compose(ApiCompletableTransformer())
    }
}
