package com.tn07.survey.features.login.transformer

import android.content.Context
import com.tn07.survey.R
import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.exceptions.ConnectionException
import com.tn07.survey.features.login.uimodel.FormError
import com.tn07.survey.features.login.uimodel.INVALID_EMAIL
import com.tn07.survey.features.login.uimodel.INVALID_PASSWORD
import com.tn07.survey.features.login.uimodel.LoginResultUiModel
import com.tn07.survey.features.login.uimodel.LoginUiModel
import com.tn07.survey.features.login.uimodel.TextFieldUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 08:52
 */
class LoginTransformerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LoginTransformer {

    override val initialLoginUiModel: LoginUiModel = LoginUiModel(
        passwordTextField = TextFieldUiModel(),
        emailTextField = TextFieldUiModel(),
        isLoading = false
    )

    override fun updateEmail(
        uiModel: LoginUiModel,
        email: String
    ): LoginUiModel {
        return if (email != uiModel.emailTextField.text) {
            uiModel.copy(
                emailTextField = uiModel.emailTextField.copy(
                    text = email,
                    error = null
                )
            )
        } else {
            uiModel
        }
    }

    override fun updatePassword(
        uiModel: LoginUiModel,
        password: String
    ): LoginUiModel {
        return if (password != uiModel.passwordTextField.text) {
            uiModel.copy(
                passwordTextField = uiModel.passwordTextField.copy(
                    text = password,
                    error = null
                )
            )
        } else {
            uiModel
        }
    }

    override fun updateFormError(
        uiModel: LoginUiModel,
        error: FormError
    ): LoginUiModel {
        val emailErrorMessage = if (error.hasError(INVALID_EMAIL)) {
            getString(R.string.error_invalid_email)
        } else {
            null
        }

        val passwordErrorMessage = if (error.hasError(INVALID_PASSWORD)) {
            getString(R.string.error_invalid_password)
        } else {
            null
        }

        return uiModel.copy(
            emailTextField = uiModel.emailTextField.copy(
                error = emailErrorMessage
            ),
            passwordTextField = uiModel.passwordTextField.copy(
                error = passwordErrorMessage
            )
        )
    }

    override fun updateLoadingStatus(uiModel: LoginUiModel, isLoading: Boolean): LoginUiModel {
        return if (uiModel.isLoading != isLoading) {
            uiModel.copy(isLoading = isLoading)
        } else {
            uiModel
        }
    }

    override fun transformErrorResult(throwable: Throwable): LoginResultUiModel.Error {
        val errorMessage = when {
            throwable is ConnectionException -> getString(R.string.error_connection)
            throwable is ApiException && throwable.httpCode == 400 -> getString(R.string.login_fail_message)
            else -> "${throwable.javaClass.name}: ${throwable.localizedMessage}"
        }

        return LoginResultUiModel.Error(errorMessage)
    }

    private fun getString(stringResId: Int): String {
        return context.resources.getString(stringResId)
    }
}