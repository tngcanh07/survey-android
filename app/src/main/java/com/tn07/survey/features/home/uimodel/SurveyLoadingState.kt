package com.tn07.survey.features.home.uimodel

/**
 * Created by toannguyen
 * Jul 18, 2021 at 19:50
 */
sealed interface SurveyLoadingState {
    val page: Int

    class Loading(
        override val page: Int
    ) : SurveyLoadingState

    class LoadSuccess(
        override val page: Int
    ) : SurveyLoadingState

    class LoadError(
        override val page: Int
    ) : SurveyLoadingState
}

val SurveyLoadingState.isFirstPage
    get() = page == 1

