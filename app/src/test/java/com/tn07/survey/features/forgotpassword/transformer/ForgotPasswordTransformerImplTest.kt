package com.tn07.survey.features.forgotpassword.transformer

import android.content.res.Resources
import androidx.test.platform.app.InstrumentationRegistry
import com.tn07.survey.R
import com.tn07.survey.domain.exceptions.ConnectionException
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
 * Jul 25, 2021 at 16:31
 */
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Config.OLDEST_SDK]
)
class ForgotPasswordTransformerImplTest {

    private lateinit var transformer: ForgotPasswordTransformerImpl
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

        transformer = ForgotPasswordTransformerImpl(context)
    }

    @Test
    fun getInitialUiModel() {
        val uiModel = transformer.initialUiModel

        Assert.assertEquals("", uiModel.email)
        Assert.assertNull(uiModel.emailErrorMessage)

        Assert.assertFalse(uiModel.isLoading)
    }


    @Test
    fun updateEmail() {
        val initialUiModel = transformer.initialUiModel
        val email = "email@mail.com"

        val uiModel = transformer.updateEmail(initialUiModel, email)

        Assert.assertEquals(email, uiModel.email)
        Assert.assertNull(uiModel.emailErrorMessage)
        Assert.assertEquals(initialUiModel.isLoading, uiModel.isLoading)
    }

    @Test
    fun updateEmail_nothingChange() {
        val initialUiModel = transformer.initialUiModel
        val email = initialUiModel.email

        val uiModel = transformer.updateEmail(initialUiModel, email)

        Assert.assertEquals(initialUiModel, uiModel)
    }

    @Test
    fun updateInvalidEmailError() {
        val initialUiModel = transformer.initialUiModel

        val uiModel = transformer.updateInvalidEmailError(initialUiModel)

        Assert.assertEquals(initialUiModel.email, initialUiModel.email)
        Assert.assertEquals(
            resources.getString(R.string.error_invalid_email),
            uiModel.emailErrorMessage
        )
        Assert.assertEquals(initialUiModel.isLoading, uiModel.isLoading)
    }

    @Test
    fun updateLoadingStatus() {
        val initialUiModel = transformer.initialUiModel

        val uiModel = transformer.updateLoadingStatus(
            initialUiModel,
            !initialUiModel.isLoading
        )
        Assert.assertEquals(!initialUiModel.isLoading, uiModel.isLoading)
        Assert.assertEquals(initialUiModel.email, uiModel.email)
        Assert.assertEquals(initialUiModel.emailErrorMessage, uiModel.emailErrorMessage)
    }

    @Test
    fun updateLoadingStatus_noChange() {
        val initialUiModel = transformer.initialUiModel

        val uiModel = transformer.updateLoadingStatus(
            initialUiModel,
            initialUiModel.isLoading
        )
        Assert.assertEquals(initialUiModel, uiModel)
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
    fun transformErrorResult_unexpectedError() {
        val exception = IllegalArgumentException("unknown")
        val error = transformer.transformErrorResult(exception)
        Assert.assertEquals(
            "${exception.javaClass.name}: ${exception.localizedMessage}",
            error.errorMessage
        )
    }
}