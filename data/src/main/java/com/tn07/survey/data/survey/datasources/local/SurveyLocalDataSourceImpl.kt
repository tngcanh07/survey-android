package com.tn07.survey.data.survey.datasources.local

import com.tn07.survey.data.db.dao.SurveyDao
import com.tn07.survey.data.db.entity.SurveyEntity
import com.tn07.survey.data.di.qualifier.SurveyDatabaseQualifier
import com.tn07.survey.domain.entities.Survey
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 24, 2021 at 23:15
 */
class SurveyLocalDataSourceImpl @Inject constructor(
    @SurveyDatabaseQualifier val surveyDao: SurveyDao
) : SurveyLocalDataSource {

    override fun saveSurveys(surveys: List<Survey>) {
        surveys.map(Survey::toSurveyEntity)
            .let(surveyDao::insertSurveys)
    }

    override fun clearSurveys() {
        surveyDao.deleteAllSurveys()
    }

    override fun getAllSurveys(): Maybe<List<Survey>> {
        return surveyDao.getAllSurveys()
            .map { it.map(SurveyEntity::toSurvey) }
    }
}