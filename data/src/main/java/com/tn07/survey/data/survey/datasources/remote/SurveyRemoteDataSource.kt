package com.tn07.survey.data.survey.datasources.remote

import com.tn07.survey.data.api.PageableApiResponse
import com.tn07.survey.data.survey.model.SurveyResponse
import io.reactivex.rxjava3.core.Single

/**
 * Created by toannguyen
 * Jul 18, 2021 at 08:59
 */
interface SurveyRemoteDataSource {
    fun getSurveys(
        pageNumber: Int,
        pageSize: Int
    ): Single<PageableApiResponse<SurveyResponse>>
}