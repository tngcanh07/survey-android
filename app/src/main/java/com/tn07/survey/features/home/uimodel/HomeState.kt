package com.tn07.survey.features.home.uimodel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 16:42
 */
sealed interface HomeState {
    object Loading : HomeState

    data class Error(
        val errorMessage: String
    ) : HomeState

    data class HomePage(
        val user: UserUiModel
    ) : HomeState
}
