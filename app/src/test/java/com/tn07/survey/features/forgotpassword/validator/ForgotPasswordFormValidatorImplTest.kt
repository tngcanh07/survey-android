package com.tn07.survey.features.forgotpassword.validator

import com.tn07.survey.features.forgotpassword.uimodel.ForgotPasswordUiModel
import com.tn07.survey.features.forgotpassword.uimodel.InvalidEmailException
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by toannguyen
 * Jul 25, 2021 at 16:39
 */
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Config.OLDEST_SDK]
)
class ForgotPasswordFormValidatorImplTest {

    private lateinit var validator: ForgotPasswordFormValidatorImpl

    @Before
    fun setUp() {
        validator = ForgotPasswordFormValidatorImpl()
    }


    @Test
    fun validateResetPasswordForm_success() {
        validator.validateResetPasswordForm(ForgotPasswordUiModel(email = "email@mail.com"))
    }

    @Test(expected = InvalidEmailException::class)
    fun validateResetPasswordForm_emailIsEmpty() {
        validator.validateResetPasswordForm(ForgotPasswordUiModel(email = ""))
    }

    @Test(expected = InvalidEmailException::class)
    fun validateResetPasswordForm_invaidEmail() {
        validator.validateResetPasswordForm(ForgotPasswordUiModel(email = "email@mail-com"))
        validator.validateResetPasswordForm(ForgotPasswordUiModel(email = "email.mail.com"))
    }
}