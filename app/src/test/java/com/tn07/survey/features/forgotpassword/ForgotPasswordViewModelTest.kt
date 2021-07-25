package com.tn07.survey.features.forgotpassword

import com.tn07.survey.domain.exceptions.ConnectionException
import com.tn07.survey.domain.usecases.RequestPasswordUseCase
import com.tn07.survey.features.TestComponent
import com.tn07.survey.features.forgotpassword.transformer.ForgotPasswordTransformer
import com.tn07.survey.features.forgotpassword.uimodel.ForgotPasswordUiModel
import com.tn07.survey.features.forgotpassword.uimodel.RequestPasswordResult
import com.tn07.survey.features.forgotpassword.validator.ForgotPasswordFormValidator
import io.reactivex.rxjava3.core.Completable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.net.UnknownHostException

/**
 * Created by toannguyen
 * Jul 25, 2021 at 16:43
 */
class ForgotPasswordViewModelTest {

    private lateinit var viewModel: ForgotPasswordViewModel

    @Mock
    private lateinit var requestPasswordUseCase: RequestPasswordUseCase

    @Mock
    private lateinit var validator: ForgotPasswordFormValidator

    @Mock
    private lateinit var transformer: ForgotPasswordTransformer

    @Mock
    private lateinit var initialUiModel: ForgotPasswordUiModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(transformer.initialUiModel).thenReturn(initialUiModel)

        viewModel = ForgotPasswordViewModel(
            requestPasswordUseCase = requestPasswordUseCase,
            validator = validator,
            transformer = transformer,
            schedulerProvider = TestComponent.schedulerProvider
        )
        viewModel.init()
    }

    @Test
    fun init() {
        viewModel.forgotPasswordUiModel.test()
            .assertValue(initialUiModel)
            .assertNoErrors()
            .assertNotComplete()

        viewModel.requestPasswordResult.test()
            .assertNoErrors()
            .assertNoValues()
            .assertNotComplete()
    }

    @Test
    fun setEmail() {
        val email = "email@mail.com"
        val nextState = Mockito.mock(ForgotPasswordUiModel::class.java)
        Mockito.`when`(transformer.updateEmail(initialUiModel, email)).thenReturn(nextState)

        viewModel.setEmail(email)

        viewModel.forgotPasswordUiModel
            .test()
            .assertValue(nextState)
            .assertNotComplete()

        viewModel.requestPasswordResult.test()
            .assertNoErrors()
            .assertNoValues()
            .assertNotComplete()
    }

    @Test
    fun requestPassword_success() {
        val email = "test@email.com"
        Mockito.`when`(initialUiModel.email).thenReturn(email)
        Mockito.`when`(requestPasswordUseCase.requestPassword(email))
            .thenReturn(Completable.complete())
        Mockito.`when`(
            transformer.updateLoadingStatus(
                Mockito.any() ?: initialUiModel,
                Mockito.anyBoolean()
            )
        )
            .thenAnswer {
                it.arguments[0]
            }

        val testObserver = viewModel.requestPasswordResult
            .test()

        viewModel.requestPassword()

        testObserver.assertValue(RequestPasswordResult.Success)
            .assertNotComplete()
    }

    @Test
    fun requestPassword_requestError() {
        val exception = ConnectionException(UnknownHostException())
        val error = RequestPasswordResult.Error("RequestPasswordResult.Error")
        val email = "test@email.com"
        Mockito.`when`(initialUiModel.email).thenReturn(email)
        Mockito.`when`(requestPasswordUseCase.requestPassword(email))
            .thenReturn(Completable.error(exception))
        Mockito.`when`(transformer.transformErrorResult(exception))
            .thenReturn(error)
        Mockito.`when`(
            transformer.updateLoadingStatus(
                Mockito.any() ?: initialUiModel,
                Mockito.anyBoolean()
            )
        )
            .thenAnswer {
                it.arguments[0]
            }

        val testObserver = viewModel.requestPasswordResult
            .test()

        viewModel.requestPassword()

        testObserver.assertValue(error)
            .assertNotComplete()
    }
}