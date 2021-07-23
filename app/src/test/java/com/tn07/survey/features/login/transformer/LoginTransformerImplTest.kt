package com.tn07.survey.features.login.transformer

import android.content.res.Resources
import androidx.test.platform.app.InstrumentationRegistry
import com.tn07.survey.R
import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.exceptions.ConnectionException
import com.tn07.survey.features.login.uimodel.FormError
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.net.SocketTimeoutException

/**
 * Created by toannguyen
 * Jul 23, 2021 at 15:34
 */
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Config.OLDEST_SDK]
)
class LoginTransformerImplTest {
    private lateinit var transformer: LoginTransformerImpl
    private lateinit var resources: Resources

    @Before
    fun setUp() {
        val context = Mockito.spy(InstrumentationRegistry.getInstrumentation().context)
        resources = Mockito.spy(context.resources)
        Mockito.`when`(context.resources).thenReturn(resources)
        Mockito.`when`(resources.getString(Mockito.anyInt() + R.string.app_name)).thenAnswer {
            val resId = it.arguments[0] as Int
            return@thenAnswer "mocked-str-id<$resId>"
        }

        transformer = LoginTransformerImpl(context)
    }

    @Test
    fun initialLoginUiModel() {
        val uiModel = transformer.initialLoginUiModel

        Assert.assertEquals("", uiModel.emailTextField.text)
        Assert.assertNull(uiModel.emailTextField.error)

        Assert.assertEquals("", uiModel.passwordTextField.text)
        Assert.assertNull(uiModel.passwordTextField.error)

        Assert.assertFalse(uiModel.isLoading)
    }

    @Test
    fun updateEmail() {
        val initialLoginUiModel = transformer.initialLoginUiModel
        val email = "email@mail.com"

        val uiModel = transformer.updateEmail(initialLoginUiModel, email)

        Assert.assertEquals(email, uiModel.emailTextField.text)
        Assert.assertNull(uiModel.emailTextField.error)
        Assert.assertEquals(initialLoginUiModel.passwordTextField, uiModel.passwordTextField)
        Assert.assertEquals(initialLoginUiModel.isLoading, uiModel.isLoading)
    }

    @Test
    fun updateEmail_nothingChange() {
        val initialLoginUiModel = transformer.initialLoginUiModel
        val email = initialLoginUiModel.emailTextField.text

        val uiModel = transformer.updateEmail(initialLoginUiModel, email)

        Assert.assertEquals(initialLoginUiModel, uiModel)
    }


    @Test
    fun updatePassword() {
        val initialLoginUiModel = transformer.initialLoginUiModel
        val password = "fake-password"

        val uiModel = transformer.updatePassword(initialLoginUiModel, password)

        Assert.assertEquals(password, uiModel.passwordTextField.text)
        Assert.assertNull(uiModel.passwordTextField.error)
        Assert.assertEquals(initialLoginUiModel.emailTextField, uiModel.emailTextField)
        Assert.assertEquals(initialLoginUiModel.isLoading, uiModel.isLoading)
    }

    @Test
    fun updatePassword_nothingChange() {
        val initialLoginUiModel = transformer.initialLoginUiModel
        val password = initialLoginUiModel.passwordTextField.text

        val uiModel = transformer.updatePassword(initialLoginUiModel, password)

        Assert.assertEquals(initialLoginUiModel, uiModel)
    }

    @Test
    fun updateFormError_email() {
        val initialLoginUiModel = transformer.initialLoginUiModel
        val error = FormError(FormError.INVALID_EMAIL)
        val uiModel = transformer.updateFormError(initialLoginUiModel, error)

        Assert.assertEquals(
            initialLoginUiModel.emailTextField.text,
            uiModel.emailTextField.text
        )
        Assert.assertNotNull(
            resources.getString(R.string.error_invalid_email),
            uiModel.emailTextField.error
        )
        Assert.assertEquals(
            initialLoginUiModel.passwordTextField.text,
            uiModel.passwordTextField.text
        )
        Assert.assertNull(uiModel.passwordTextField.error)
        Assert.assertEquals(initialLoginUiModel.isLoading, uiModel.isLoading)
    }

    @Test
    fun updateFormError_password() {
        val initialLoginUiModel = transformer.initialLoginUiModel
        val error = FormError(FormError.INVALID_PASSWORD)
        val uiModel = transformer.updateFormError(initialLoginUiModel, error)

        Assert.assertEquals(
            initialLoginUiModel.passwordTextField.text,
            uiModel.passwordTextField.text
        )
        Assert.assertEquals(
            resources.getString(R.string.error_invalid_password),
            uiModel.passwordTextField.error
        )
        Assert.assertEquals(
            initialLoginUiModel.emailTextField.text,
            uiModel.emailTextField.text
        )
        Assert.assertNull(uiModel.emailTextField.error)
        Assert.assertEquals(initialLoginUiModel.isLoading, uiModel.isLoading)
    }

    @Test
    fun updateLoadingStatus() {
        val initialLoginUiModel = transformer.initialLoginUiModel

        val uiModel = transformer.updateLoadingStatus(
            initialLoginUiModel,
            !initialLoginUiModel.isLoading
        )
        Assert.assertEquals(!initialLoginUiModel.isLoading, uiModel.isLoading)
        Assert.assertEquals(initialLoginUiModel.emailTextField, uiModel.emailTextField)
        Assert.assertEquals(initialLoginUiModel.passwordTextField, uiModel.passwordTextField)
    }

    @Test
    fun updateLoadingStatus_noChange() {
        val initialLoginUiModel = transformer.initialLoginUiModel

        val uiModel = transformer.updateLoadingStatus(
            initialLoginUiModel,
            initialLoginUiModel.isLoading
        )
        Assert.assertEquals(initialLoginUiModel, uiModel)
    }

    @Test
    fun transformErrorResult_connectionException() {
        val exception = ConnectionException(SocketTimeoutException())
        val error = transformer.transformErrorResult(exception)
        Assert.assertEquals(
            resources.getString(R.string.error_connection),
            error.errorMessage
        )
    }

    @Test
    fun transformErrorResult_invalidCredentials() {
        val exception = ApiException(400)
        val error = transformer.transformErrorResult(exception)
        Assert.assertEquals(
            resources.getString(R.string.login_fail_message),
            error.errorMessage
        )
    }

    @Test
    fun transformErrorResult_unexpectedError() {
        val exception = IllegalArgumentException("unknown")
        val error = transformer.transformErrorResult(exception)
        Assert.assertEquals(
            "${exception.javaClass.name}: ${exception.localizedMessage}",
            error.errorMessage
        )
    }
}