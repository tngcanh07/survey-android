package com.tn07.survey.features.forgotpassword.transformer

import com.tn07.survey.features.forgotpassword.uimodel.ForgotPasswordUiModel
import com.tn07.survey.features.forgotpassword.uimodel.ResetPasswordResult

/**
 * Created by toannguyen
 * Jul 25, 2021 at 15:29
 */
interface ForgotPasswordTransformer {

    val initialUiModel: ForgotPasswordUiModel

    fun updateEmail(
        uiModel: ForgotPasswordUiModel,
        email: String
    ): ForgotPasswordUiModel

    fun updateLoadingStatus(
        uiModel: ForgotPasswordUiModel,
        isLoading: Boolean
    ): ForgotPasswordUiModel

    fun updateInvalidEmailError(uiModel: ForgotPasswordUiModel): ForgotPasswordUiModel

    fun transformErrorResult(throwable: Throwable): ResetPasswordResult.Error
}