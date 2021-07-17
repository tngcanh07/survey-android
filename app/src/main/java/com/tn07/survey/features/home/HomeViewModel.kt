package com.tn07.survey.features.home

import com.tn07.survey.domain.usecases.GetUserUseCase
import com.tn07.survey.domain.usecases.LogoutUseCase
import com.tn07.survey.features.base.BaseViewModel
import com.tn07.survey.features.home.transformer.HomeTransformer
import com.tn07.survey.features.home.uimodel.HomeState
import com.tn07.survey.features.home.uimodel.UserUiModel
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
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val transformer: HomeTransformer
) : BaseViewModel() {
    private val userObservable: Observable<UserUiModel>
        get() = getUserUseCase.getUserObservable()
            .map(transformer::transformUser)

    private val _loadingState = BehaviorSubject.createDefault(false)
    val loadingState: Observable<Boolean>
        get() = _loadingState.distinctUntilChanged()

    private val _homeState = BehaviorSubject.create<HomeState>()
    val homeState: Observable<HomeState>
        get() = _homeState.distinctUntilChanged()

    fun loadHomePage() {
        userObservable
            .doOnSubscribe {
                _homeState.onNext(HomeState.Loading)
            }
            .subscribeOn(Schedulers.io())
            .map(transformer::transformInitState)
            .subscribe(_homeState::onNext) {
                _homeState.onNext(HomeState.Error("${it.javaClass.name} ${it.localizedMessage}"))
            }
            .addToCompositeDisposable()
    }

    fun logout() {
        logoutUseCase.logout()
            .doOnSubscribe { onStartLogout() }
            .subscribeOn(Schedulers.io())
            .subscribe(::onLoggedOutSuccessfully, ::onLogoutFailed)
            .addToCompositeDisposable()
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