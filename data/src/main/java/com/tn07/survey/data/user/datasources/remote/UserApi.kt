package com.tn07.survey.data.user.datasources.remote

import com.tn07.survey.data.api.ApiResponse
import com.tn07.survey.data.user.datasources.remote.responsemodel.UserResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:13
 */
interface UserApi {
    @GET("me")
    fun getUser(): Single<Response<ApiResponse<UserResponse>>>
}