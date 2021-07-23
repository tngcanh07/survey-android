package com.tn07.survey.features.login.validator

import com.tn07.survey.features.login.uimodel.FormError
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by toannguyen
 * Jul 23, 2021 at 16:15
 */
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Config.OLDEST_SDK]
)
class LogInFormValidatorImplTest {

    private lateinit var validator: LogInFormValidatorImpl

    @Before
    fun setUp() {
        validator = LogInFormValidatorImpl()
    }

    @Test
    fun validateLoginForm_success() {
        validator.validateLoginForm("email@mail.com", "password")
    }

    @Test(expected = FormError::class)
    fun validateLoginForm_invalidEmail() {
        validator.validateLoginForm("email@mail-com", "password")
    }

    @Test(expected = FormError::class)
    fun validateLoginForm_invalidPassword() {
        validator.validateLoginForm("email@mail.com", "")
    }

    @Test(expected = FormError::class)
    fun validateLoginForm_invalidEmailAndInvalidPassword() {
        validator.validateLoginForm("email@mail-com", "")
    }
}