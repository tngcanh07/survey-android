package com.tn07.survey.features.forgotpassword.transformer

import android.content.Context
import com.tn07.survey.R
import com.tn07.survey.domain.exceptions.ConnectionException
import com.tn07.survey.features.forgotpassword.uimodel.ForgotPasswordUiModel
import com.tn07.survey.features.forgotpassword.uimodel.ResetPasswordResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 25, 2021 at 15:29
 */
class ForgotPasswordTransformerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ForgotPasswordTransformer {

    override val initialUiModel: ForgotPasswordUiModel
        get() = ForgotPasswordUiModel()

    override fun updateEmail(
        uiModel: ForgotPasswordUiModel,
        email: String
    ): ForgotPasswordUiModel {
        return if (email != uiModel.email) {
            uiModel.copy(
                email = email,
                emailErrorMessage = null
            )
        } else {
            uiModel
        }
    }

    override fun updateLoadingStatus(
        uiModel: ForgotPasswordUiModel,
        isLoading: Boolean
    ): ForgotPasswordUiModel {
        return if (uiModel.isLoading != isLoading) {
            uiModel.copy(isLoading = isLoading)
        } else {
            uiModel
        }
    }

    override fun updateInvalidEmailError(uiModel: ForgotPasswordUiModel): ForgotPasswordUiModel {
        val errorMessage = getString(R.string.error_invalid_email)
        return if (uiModel.emailErrorMessage != errorMessage) {
            uiModel.copy(emailErrorMessage = errorMessage)
        } else {
            uiModel
        }
    }

    override fun transformErrorResult(throwable: Throwable): ResetPasswordResult.Error {
        val errorMessage = when (throwable) {
            is ConnectionException -> getString(R.string.error_connection)
            else -> "${throwable.javaClass.name}: ${throwable.localizedMessage}"
        }
        return ResetPasswordResult.Error(errorMessage)
    }

    private fun getString(stringResId: Int): String {
        return context.resources.getString(stringResId)
    }
}