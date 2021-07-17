package com.tn07.survey.features.login.validator

import android.util.Patterns
import com.tn07.survey.features.login.uimodel.FormError
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 09:04
 */
class LogInFormValidatorImpl @Inject constructor() : LogInFormValidator {

    override fun validateLoginForm(email: String, password: String) {
        val errorFlags = validatePassword(password) or validateEmail(email)
        if (errorFlags != 0) {
            throw FormError(errorFlags)
        }
    }

    private fun validatePassword(password: String): Int {
        return if (password.isEmpty()) {
            FormError.INVALID_PASSWORD
        } else {
            0
        }
    }

    private fun validateEmail(email: String): Int {
        return if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).find()) {
            FormError.INVALID_EMAIL
        } else {
            0
        }
    }
}