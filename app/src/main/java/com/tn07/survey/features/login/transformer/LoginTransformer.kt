package com.tn07.survey.features.login.transformer

import com.tn07.survey.features.login.uimodel.FormError
import com.tn07.survey.features.login.uimodel.LoginResultUiModel
import com.tn07.survey.features.login.uimodel.LoginUiModel

/**
 * Created by toannguyen
 * Jul 17, 2021 at 08:52
 */
interface LoginTransformer {

    val initialLoginUiModel: LoginUiModel

    fun updateEmail(
        uiModel: LoginUiModel,
        email: String
    ): LoginUiModel

    fun updatePassword(
        uiModel: LoginUiModel,
        password: String
    ): LoginUiModel

    fun updateFormError(
        uiModel: LoginUiModel,
        error: FormError
    ): LoginUiModel

    fun updateLoadingStatus(
        uiModel: LoginUiModel,
        isLoading: Boolean
    ): LoginUiModel

    fun transformErrorResult(throwable: Throwable): LoginResultUiModel.Error
}