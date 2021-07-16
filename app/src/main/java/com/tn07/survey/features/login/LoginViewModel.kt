package com.tn07.survey.features.login

import androidx.lifecycle.ViewModel
import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.usecases.GetUserUseCase
import com.tn07.survey.domain.usecases.LoginUseCase
import com.tn07.survey.features.login.uimodel.LoginResultUiModel
import com.tn07.survey.features.login.uimodel.LoginUiModel
import com.tn07.survey.features.login.uimodel.TextFieldUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
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
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _loginResult = PublishSubject.create<LoginResultUiModel>()
    val loginResult: Observable<LoginResultUiModel>
        get() = _loginResult

    private val _loginUiModel = BehaviorSubject.create<LoginUiModel>()
    val loginUiModel: Observable<LoginUiModel>
        get() = _loginUiModel

    val loginState: Observable<Boolean>
        get() = getUserUseCase.getUserObservable()
            .map { it is AccessToken }
            .distinctUntilChanged()


    private val loginUiModelEvents = PublishSubject.create<(LoginUiModel) -> LoginUiModel>()

    fun init() {
        val initialUiModel = LoginUiModel(
            passwordTextField = TextFieldUiModel(),
            emailTextField = TextFieldUiModel(),
            isLoading = false
        )
        _loginUiModel.onNext(initialUiModel)

        loginUiModelEvents.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .subscribe {
                _loginUiModel.value
                    ?.let(it)
                    ?.let(_loginUiModel::onNext)
            }
            .let(::addToCompositeDisposable)

    }

    val isLoggedIn: Boolean
        get() = getUserUseCase.getUser() is AccessToken

    fun login(email: String, password: String) {
        loginUseCase.login(email = email, password = password)
            .doOnSubscribe { onStartLogIn(email = email, password = password) }
            .subscribe(::onLoginSuccess, ::onLoginError)
            .let(::addToCompositeDisposable)
    }

    private fun onLoginSuccess() {
        _loginResult.onNext(LoginResultUiModel.Success)
        loginUiModelEvents.onNext {
            it.copy(isLoading = false)
        }
    }

    private fun onStartLogIn(email: String, password: String) {
        loginUiModelEvents.onNext {
            it.copy(
                emailTextField = it.emailTextField.copy(text = email),
                passwordTextField = it.passwordTextField.copy(text = password),
                isLoading = true
            )
        }
    }

    private fun onLoginError(throwable: Throwable) {
        _loginResult.onNext(LoginResultUiModel.Error(throwable.localizedMessage ?: "Unknown"))
        loginUiModelEvents.onNext {
            it.copy(isLoading = false)
        }
    }

    @Synchronized
    fun addToCompositeDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}