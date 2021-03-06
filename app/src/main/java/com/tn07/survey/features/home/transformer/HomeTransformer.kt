package com.tn07.survey.features.home.transformer

import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.entities.User
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import com.tn07.survey.features.home.uimodel.UserUiModel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 16:41
 */
interface HomeTransformer {
    val todayDateTime: String

    fun transformUser(user: User): UserUiModel

    fun transformSurvey(survey: Survey): SurveyUiModel

    fun transformErrorMessage(throwable: Throwable): String
}