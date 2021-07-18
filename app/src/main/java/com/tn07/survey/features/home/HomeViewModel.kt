package com.tn07.survey.features.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava3.flowable
import com.tn07.survey.domain.usecases.GetSurveyUseCase
import com.tn07.survey.domain.usecases.GetUserUseCase
import com.tn07.survey.domain.usecases.LogoutUseCase
import com.tn07.survey.features.base.BaseViewModel
import com.tn07.survey.features.home.pagingsource.SurveyPagingSource
import com.tn07.survey.features.home.transformer.HomeTransformer
import com.tn07.survey.features.home.uimodel.HomeState
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import com.tn07.survey.features.home.uimodel.UserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
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
    private val getSurveyUseCase: GetSurveyUseCase,
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

    private var currentPagingSource: SurveyPagingSource? = null
    private val pagingConfig: PagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        maxSize = MAX_CAPACITY,
        initialLoadSize = PAGE_SIZE,
        prefetchDistance = PREFETCH_DISTANCE
    )

    val surveyListResult: Flowable<PagingData<SurveyUiModel>> = Pager(pagingConfig) {
        SurveyPagingSource(
            getSurveyUseCase = getSurveyUseCase,
            startPageIndex = START_PAGE_INDEX
        ).also {
            currentPagingSource = it
        }
    }
        .flowable
        .map {
            it.map(transformer::transformSurvey)
        }


    companion object {
        const val START_PAGE_INDEX = 1
        const val PAGE_SIZE = 2
        const val PREFETCH_DISTANCE = 1
        const val MAX_CAPACITY = 20
    }
}