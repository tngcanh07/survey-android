package com.tn07.survey.features.home.uimodel

/**
 * Created by toannguyen
 * Jul 18, 2021 at 19:50
 */
sealed interface HomeState {
    object Loading : HomeState

    class Survey(
        val isLoadingNext: Boolean
    ) : HomeState

    class Error(
        val errorMessage: String
    ) : HomeState
}
