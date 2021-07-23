package com.tn07.survey.features.login.validator

/**
 * Created by toannguyen
 * Jul 17, 2021 at 09:04
 */

interface LogInFormValidator {
    fun validateLoginForm(email: String, password: String)
}
