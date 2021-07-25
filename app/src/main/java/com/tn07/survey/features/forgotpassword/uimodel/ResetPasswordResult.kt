package com.tn07.survey.features.forgotpassword.uimodel

/**
 * Created by toannguyen
 * Jul 25, 2021 at 15:14
 */
sealed interface ResetPasswordResult {

    object Success : ResetPasswordResult

    class Error(
        val errorMessage: String
    ) : ResetPasswordResult
}