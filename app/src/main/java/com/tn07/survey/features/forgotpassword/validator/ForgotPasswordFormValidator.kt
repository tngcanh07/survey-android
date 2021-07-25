package com.tn07.survey.features.forgotpassword.validator

import com.tn07.survey.features.forgotpassword.uimodel.ForgotPasswordUiModel

/**
 * Created by toannguyen
 * Jul 25, 2021 at 15:20
 */
interface ForgotPasswordFormValidator {

    fun validateResetPasswordForm(uiModel: ForgotPasswordUiModel)
}