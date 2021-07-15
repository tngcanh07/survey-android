package com.tn07.survey.features.login

import androidx.lifecycle.ViewModel
import com.tn07.survey.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 15, 2021 at 09:04
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun login(email: String, password: String) {
        loginUseCase.login(email = email, password = password)
            .doOnSubscribe { onStartLogIn() }
            .subscribe(::onLoginSuccess, ::onLoginError)
            .let(::addToDisposable)
    }

    private fun onLoginSuccess() {

    }

    private fun onStartLogIn() {

    }

    private fun onLoginError(throwable: Throwable) {

    }

    @Synchronized
    fun addToDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}