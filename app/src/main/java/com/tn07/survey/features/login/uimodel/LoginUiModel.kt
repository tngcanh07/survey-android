package com.tn07.survey.features.login.uimodel

/**
 * Created by toannguyen
 * Jul 16, 2021 at 10:02
 */
data class LoginUiModel(
    val passwordTextField: TextFieldUiModel,
    val emailTextField: TextFieldUiModel,
    val isLoading: Boolean
)
