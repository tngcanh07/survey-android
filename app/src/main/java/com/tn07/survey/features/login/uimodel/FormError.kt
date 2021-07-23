package com.tn07.survey.features.login.uimodel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 09:21
 */
const val INVALID_EMAIL = 1
const val INVALID_PASSWORD = 1 shl 1

class FormError(
    private val errorFlags: Int
) : Exception() {

    fun hasError(errorFlag: Int): Boolean {
        return (errorFlags and errorFlag) == errorFlag
    }
}