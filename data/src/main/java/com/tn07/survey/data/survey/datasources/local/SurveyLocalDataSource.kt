package com.tn07.survey.data.survey.datasources.local

import com.tn07.survey.domain.entities.Survey
import io.reactivex.rxjava3.core.Maybe

/**
 * Created by toannguyen
 * Jul 24, 2021 at 23:14
 */
interface SurveyLocalDataSource {
    fun saveSurveys(surveys: List<Survey>)

    fun clearSurveys()

    fun getAllSurveys(): Maybe<List<Survey>>
}