package com.tn07.survey.data.survey.datasources.remote

import com.tn07.survey.data.api.PageableApiResponse
import com.tn07.survey.data.api.transfomers.ApiSingleTransformer
import com.tn07.survey.data.survey.model.SurveyResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 18, 2021 at 09:00
 */
class SurveyRemoteDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
) : SurveyRemoteDataSource {

    private val surveyApi: SurveyApi by lazy {
        retrofit.create(SurveyApi::class.java)
    }

    override fun getSurveys(
        pageNumber: Int,
        pageSize: Int
    ): Single<PageableApiResponse<SurveyResponse>> {
        return surveyApi.getSurveys(pageNumber = pageNumber, pageSize = pageSize)
            .compose(ApiSingleTransformer())
    }
}