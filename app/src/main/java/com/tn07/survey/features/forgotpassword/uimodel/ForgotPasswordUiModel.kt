package com.tn07.survey.features.forgotpassword.uimodel

/**
 * Created by toannguyen
 * Jul 25, 2021 at 15:13
 */
data class ForgotPasswordUiModel(
    val email: String = "",
    val emailErrorMessage: String? = null,
    val isLoading: Boolean = false
)