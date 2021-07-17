package com.tn07.survey.features.home

import com.tn07.survey.domain.usecases.LogoutUseCase
import com.tn07.survey.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 15:59
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel() {

    private val _loadingState = BehaviorSubject.createDefault(false)
    val loadingState: Observable<Boolean>
        get() = _loadingState.distinctUntilChanged()

    fun logout() {
        logoutUseCase.logout()
            .doOnSubscribe { onStartLogout() }
            .subscribeOn(Schedulers.io())
            .subscribe(::onLoggedOutSuccessfully, ::onLogoutFailed)
    }

    private fun onStartLogout() {
        _loadingState.onNext(true)
    }

    private fun onLogoutFailed(throwable: Throwable) {
        _loadingState.onNext(false)
    }

    private fun onLoggedOutSuccessfully() {
        _loadingState.onNext(false)
    }
}