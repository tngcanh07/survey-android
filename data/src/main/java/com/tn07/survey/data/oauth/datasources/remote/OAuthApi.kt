package com.tn07.survey.data.oauth.datasources.remote

import com.tn07.survey.data.api.ApiResponse
import com.tn07.survey.data.oauth.datasources.remote.requestmodel.RefreshTokenRequestModel
import com.tn07.survey.data.oauth.datasources.remote.requestmodel.RequestPasswordRequestModel
import com.tn07.survey.data.oauth.datasources.remote.requestmodel.RequestTokenRequestModel
import com.tn07.survey.data.oauth.datasources.remote.requestmodel.RevokeTokenRequestModel
import com.tn07.survey.data.oauth.datasources.remote.responsemodel.AccessTokenResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:10
 */
interface OAuthApi {
    @POST("oauth/token")
    fun requestToken(
        @Body requestModel: RequestTokenRequestModel
    ): Single<Response<ApiResponse<AccessTokenResponse>>>

    @POST("oauth/token")
    fun refreshToken(
        @Body requestModel: RefreshTokenRequestModel
    ): Single<Response<ApiResponse<AccessTokenResponse>>>

    @POST("oauth/revoke")
    fun revokeToken(@Body requestModel: RevokeTokenRequestModel): Completable

    @POST("passwords")
    fun requestPassword(@Body requestModel: RequestPasswordRequestModel): Completable
}