package com.tn07.survey.domain.usecases

import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

/**
 * Created by toannguyen
 * Jul 18, 2021 at 08:47
 */
interface GetSurveyUseCase {
    fun getSurveys(
        page: Int,
        pageSize: Int
    ): Single<Pageable<Survey>>

    fun getLocalSurveys(): Maybe<List<Survey>>
}