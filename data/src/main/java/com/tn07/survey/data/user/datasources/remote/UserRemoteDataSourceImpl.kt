package com.tn07.survey.data.user.datasources.remote

import com.tn07.survey.data.api.transfomers.ApiSingleTransformer
import com.tn07.survey.data.user.models.UserDataModel
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:24
 */
class UserRemoteDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
) : UserRemoteDataSource {
    private val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    override fun getUser(): Single<UserDataModel> {
        return userApi.getUser()
            .compose(ApiSingleTransformer())
            .map { it.data.user }
    }
}