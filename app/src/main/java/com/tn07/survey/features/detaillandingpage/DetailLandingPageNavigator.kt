package com.tn07.survey.features.detaillandingpage

/**
 * Created by toannguyen
 * Jul 18, 2021 at 14:39
 */
interface DetailLandingPageNavigator {
    fun navigateBackFromLandingPage()

    fun navigateToSurveyDetail(surveyId: String)
}