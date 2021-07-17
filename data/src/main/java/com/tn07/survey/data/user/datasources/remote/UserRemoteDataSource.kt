package com.tn07.survey.data.user.datasources.remote

import com.tn07.survey.data.user.models.UserDataModel
import io.reactivex.rxjava3.core.Single

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:13
 */
interface UserRemoteDataSource {
    fun getUser(): Single<UserDataModel>
}