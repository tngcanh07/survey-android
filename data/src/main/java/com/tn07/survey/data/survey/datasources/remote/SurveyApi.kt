package com.tn07.survey.data.survey.datasources.remote

import com.tn07.survey.data.api.PageableApiResponse
import com.tn07.survey.data.survey.model.SurveyResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by toannguyen
 * Jul 18, 2021 at 09:16
 */
interface SurveyApi {
    @GET("surveys")
    fun getSurveys(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") pageSize: Int
    ): Single<Response<PageableApiResponse<SurveyResponse>>>
}