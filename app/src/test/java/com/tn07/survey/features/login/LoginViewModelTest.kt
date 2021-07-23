package com.tn07.survey.features.login

import com.tn07.survey.domain.exceptions.ApiException
import com.tn07.survey.domain.usecases.GetTokenUseCase
import com.tn07.survey.domain.usecases.LoginUseCase
import com.tn07.survey.features.TestComponent
import com.tn07.survey.features.login.transformer.LoginTransformer
import com.tn07.survey.features.login.uimodel.LoginResultUiModel
import com.tn07.survey.features.login.uimodel.LoginUiModel
import com.tn07.survey.features.login.uimodel.TextFieldUiModel
import com.tn07.survey.features.login.validator.LogInFormValidator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by toannguyen
 * Jul 23, 2021 at 16:21
 */
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    @Mock
    private lateinit var loginUseCase: LoginUseCase

    @Mock
    private lateinit var getTokenUseCase: GetTokenUseCase

    @Mock
    private lateinit var formValidator: LogInFormValidator

    @Mock
    private lateinit var transformer: LoginTransformer

    private val initialLoginUiModel: LoginUiModel = LoginUiModel(
        passwordTextField = TextFieldUiModel(),
        emailTextField = TextFieldUiModel(),
        isLoading = false
    )

    private lateinit var loginUiModelObserver: TestObserver<LoginUiModel>
    private lateinit var loginResultObserver: TestObserver<LoginResultUiModel>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(transformer.initialLoginUiModel).thenReturn(initialLoginUiModel)
        loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            getTokenUseCase = getTokenUseCase,
            formValidator = formValidator,
            transformer = transformer,
            schedulerProvider = TestComponent.schedulerProvider
        )
        loginUiModelObserver = loginViewModel.loginUiModel.test()
        loginResultObserver = loginViewModel.loginResult.test()

        loginViewModel.init()
    }

    @Test
    fun init() {
        loginUiModelObserver.awaitCount(1)
            .assertValue(initialLoginUiModel)
            .assertNotComplete()
    }

    @Test
    fun setEmail() {
        val email = "email@mail.com"
        val nextState = Mockito.mock(LoginUiModel::class.java)
        Mockito.`when`(transformer.updateEmail(initialLoginUiModel, email)).thenReturn(nextState)

        loginViewModel.setEmail(email)

        loginUiModelObserver.awaitCount(2)
            .assertValues(nextState)
            .assertNotComplete()
    }

    @Test
    fun setPassword() {
        val password = "password"
        val nextState = Mockito.mock(LoginUiModel::class.java)
        Mockito.`when`(transformer.updatePassword(initialLoginUiModel, password))
            .thenReturn(nextState)

        loginViewModel.setPassword(password)

        loginUiModelObserver.awaitCount(2)
            .assertValues(nextState)
            .assertNotComplete()
    }

    @Test
    fun login_success() {
        val email = initialLoginUiModel.emailTextField.text
        val password = initialLoginUiModel.passwordTextField.text
        Mockito.`when`(loginUseCase.login(email, password)).thenReturn(Completable.complete())

        loginViewModel.login()

        loginResultObserver.awaitCount(2)
            .assertValue(LoginResultUiModel.Success)
    }

    @Test
    fun login_error() {
        val email = initialLoginUiModel.emailTextField.text
        val password = initialLoginUiModel.passwordTextField.text
        val expectedException = ApiException(400)
        val expectedError = LoginResultUiModel.Error("LoginResultUiModel.Error")
        Mockito.`when`(loginUseCase.login(email, password))
            .thenReturn(Completable.error(expectedException))
        Mockito.`when`(transformer.transformErrorResult(expectedException))
            .thenReturn(expectedError)

        loginViewModel.login()

        loginResultObserver.awaitCount(2)
            .assertValue(expectedError)
    }
}