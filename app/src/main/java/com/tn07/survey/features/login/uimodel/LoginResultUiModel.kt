package com.tn07.survey.features.login.uimodel

sealed interface LoginResultUiModel {

    object Success : LoginResultUiModel

    class Error(
        val errorMessage: String
    ) : LoginResultUiModel
}