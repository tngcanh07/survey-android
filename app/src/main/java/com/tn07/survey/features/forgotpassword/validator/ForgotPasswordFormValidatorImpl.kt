package com.tn07.survey.features.forgotpassword.validator

import android.util.Patterns
import com.tn07.survey.features.forgotpassword.uimodel.ForgotPasswordUiModel
import com.tn07.survey.features.forgotpassword.uimodel.InvalidEmailException
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 25, 2021 at 15:20
 */
class ForgotPasswordFormValidatorImpl @Inject constructor() : ForgotPasswordFormValidator {

    override fun validateResetPasswordForm(uiModel: ForgotPasswordUiModel) {
        val email = uiModel.email
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).find()) {
            throw InvalidEmailException()
        }
    }
}