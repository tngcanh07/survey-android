package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.Survey
import io.reactivex.rxjava3.core.Single

/**
 * Created by toannguyen
 * Jul 18, 2021 at 08:47
 */
interface GetSurveyUseCase {
    fun getSurveys(
        page: Int,
        pageSize: Int
    ): Single<List<Survey>>
}