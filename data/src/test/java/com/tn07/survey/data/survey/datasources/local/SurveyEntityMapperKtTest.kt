package com.tn07.survey.data.survey.datasources.local

import com.tn07.survey.data.TestDataProvider
import org.junit.Test

/**
 * Created by toannguyen
 * Jul 25, 2021 at 11:42
 */
class SurveyEntityMapperKtTest {

    @Test
    fun toSurveyEntity() {
        val survey = TestDataProvider.generateSurvey()

        val result = survey.toSurveyEntity()

        TestDataProvider.assertSurvey(survey, result)
    }

    @Test
    fun toSurveyEntity_alreadySurveyEntity_doNothing() {
        val survey = TestDataProvider.generaSurveyEntity()

        val result = survey.toSurveyEntity()

        TestDataProvider.assertSurvey(survey, result)
    }

    @Test
    fun toSurvey() {
        val survey = TestDataProvider.generaSurveyEntity()

        val result = survey.toSurvey()

        TestDataProvider.assertSurvey(survey, result)
    }
}