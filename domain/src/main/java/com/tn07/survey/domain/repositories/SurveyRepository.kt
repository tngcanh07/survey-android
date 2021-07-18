package com.tn07.survey.domain.repositories

import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey
import io.reactivex.rxjava3.core.Single

/**
 * Created by toannguyen
 * Jul 18, 2021 at 08:52
 */
interface SurveyRepository {
    fun getSurveys(page: Int, pageSize: Int): Single<Pageable<Survey>>
}