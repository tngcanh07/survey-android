package com.tn07.survey.data.api.mock

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

/**
 * Created by toannguyen
 * Jul 23, 2021 at 11:11
 */
interface ApiService {
    @GET("/data")
    fun getUserInfo(): Single<TestData>
}

class TestData(val data: String)