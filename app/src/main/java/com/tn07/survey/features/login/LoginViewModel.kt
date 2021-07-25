package com.tn07.survey.features.login

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.usecases.GetTokenUseCase
import com.tn07.survey.domain.usecases.LoginUseCase
import com.tn07.survey.features.base.BaseViewModel
import com.tn07.survey.features.common.SchedulerProvider
import com.tn07.survey.features.login.transformer.LoginTransformer
import com.tn07.survey.features.login.uimodel.FormError
import com.tn07.survey.features.login.uimodel.LoginResultUiModel
import com.tn07.survey.features.login.uimodel.LoginUiModel
import com.tn07.survey.features.login.validator.LogInFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 15, 2021 at 09:04
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val formValidator: LogInFormValidator,
    private val transformer: LoginTransformer,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _loginResult = PublishSubject.create<LoginResultUiModel>()
    val loginResult: Observable<LoginResultUiModel>
        get() = _loginResult

    private val _loginUiModel = BehaviorSubject.createDefault(transformer.initialLoginUiModel)
    val loginUiModel: Observable<LoginUiModel>
        get() = _loginUiModel.distinctUntilChanged()

    val loginState: Observable<Boolean>
        get() = getTokenUseCase.getTokenObservable()
            .map { it is AccessToken }
            .distinctUntilChanged()

    private val loginUiModelEvents = PublishSubject.create<(LoginUiModel) -> LoginUiModel>()

    fun init() {
        loginUiModelEvents.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(schedulerProvider.io())
            .subscribe {
                _loginUiModel.value
                    ?.let(it)
                    ?.let(_loginUiModel::onNext)
            }
            .addToCompositeDisposable()
    }

    fun setEmail(email: String) {
        loginUiModelEvents.onNext {
            transformer.updateEmail(it, email)
        }
    }

    fun setPassword(password: String) {
        loginUiModelEvents.onNext {
            transformer.updatePassword(it, password)
        }
    }

    fun login() {
        Single.fromCallable {
            val uiModel = _loginUiModel.value
            val email = uiModel.emailTextField.text
            val password = uiModel.passwordTextField.text
            formValidator.validateLoginForm(email = email, password = password)
            uiModel
        }.flatMapCompletable { uiModel ->
            loginUseCase.login(
                email = uiModel.emailTextField.text,
                password = uiModel.passwordTextField.text
            )
        }
            .subscribeOn(schedulerProvider.io())
            .doOnSubscribe { onStartLogIn() }
            .subscribe(::onLoginSuccess, ::onLoginError)
            .addToCompositeDisposable()
    }

    private fun onLoginSuccess() {
        _loginResult.onNext(LoginResultUiModel.Success)
        loginUiModelEvents.onNext {
            transformer.updateLoadingStatus(it, false)
        }
    }

    private fun onStartLogIn() {
        loginUiModelEvents.onNext {
            transformer.updateLoadingStatus(it, true)
        }
    }

    private fun onLoginError(throwable: Throwable) {
        loginUiModelEvents.onNext {
            transformer.updateLoadingStatus(it, false)
        }
        if (throwable is FormError) {
            loginUiModelEvents.onNext {
                transformer.updateFormError(it, throwable)
            }
        } else {
            _loginResult.onNext(transformer.transformErrorResult(throwable))
        }
    }
}