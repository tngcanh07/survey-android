package com.tn07.survey.features.home

import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.usecases.GetSurveyUseCase
import com.tn07.survey.domain.usecases.GetUserUseCase
import com.tn07.survey.domain.usecases.LogoutUseCase
import com.tn07.survey.features.base.BaseViewModel
import com.tn07.survey.features.common.SchedulerProvider
import com.tn07.survey.features.home.transformer.HomeTransformer
import com.tn07.survey.features.home.uimodel.HomeState
import com.tn07.survey.features.home.uimodel.LogoutResultUiModel
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import com.tn07.survey.features.home.uimodel.UserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 15:59
 */
private const val FIRST_PAGE_INDEX = 1
private const val PAGE_SIZE = 5
private const val PREFETCH_OFFSET = 1

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getSurveyUseCase: GetSurveyUseCase,
    private val transformer: HomeTransformer,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _homeState = BehaviorSubject.create<HomeState>()
    val surveyLoadingEvents: Observable<HomeState>
        get() = _homeState

    private val _user = BehaviorSubject.create<UserUiModel>()
    val user: Observable<UserUiModel>
        get() = _user.distinctUntilChanged()

    private val _surveyListResult = BehaviorSubject.createDefault(SurveyData())
    val surveyListResult: Flowable<List<SurveyUiModel>>
        get() = _surveyListResult.toFlowable(BackpressureStrategy.LATEST)
            .map(SurveyData::items)
            .distinctUntilChanged()

    private val hasMore: Boolean
        get() {
            val surveyData = _surveyListResult.value
            return surveyData.totalPage == null || surveyData.page < surveyData.totalPage
        }

    private var loadingSurveyDisposable: Disposable? = null

    private var currentSurveyItem: Int = 0

    val todayDateTime: String
        get() = transformer.todayDateTime

    private val _errorMessageObservable = PublishSubject.create<String>()
    val errorMessageObservable: Observable<String>
        get() = _errorMessageObservable

    fun initUser() {
        if (_user.value == null) {
            loadUser()
        }
    }

    fun initSurveys() {
        if (_surveyListResult.value.totalPage == null) {
            loadingSurveyDisposable = getSurveyUseCase.getLocalSurveys()
                .doOnSuccess {
                    val surveyUiModels = it.map(transformer::transformSurvey)
                    val surveyData = _surveyListResult.value
                    if (surveyData.totalPage == null) {
                        val localSurveyData = surveyData.copy(items = surveyUiModels)
                        _surveyListResult.onNext(localSurveyData)
                        _homeState.onNext(HomeState.Survey(isLoadingNext = false))
                    }
                }
                .ignoreElement()
                .onErrorComplete()
                .subscribeOn(schedulerProvider.io())
                .subscribe {
                    checkAndLoadSurveyIfNeeded()
                }
        } else {
            checkAndLoadSurveyIfNeeded()
        }
    }

    private fun loadUser() {
        getUserUseCase.getUserObservable()
            .map(transformer::transformUser)
            .subscribeOn(schedulerProvider.io())
            .subscribe(_user::onNext) {
            }
            .addToCompositeDisposable()
    }

    fun logout(): Single<LogoutResultUiModel> {
        return logoutUseCase.logout()
            .andThen(Single.just<LogoutResultUiModel>(LogoutResultUiModel.Success))
            .onErrorResumeNext {
                Single.just(LogoutResultUiModel.Error)
            }
    }

    private fun mergeSurveyData(data: Pageable<Survey>) {
        val currentState = _surveyListResult.value

        val items = if (data.page == FIRST_PAGE_INDEX) {
            data.items.map(transformer::transformSurvey)
        } else {
            currentState.items + data.items.map(transformer::transformSurvey)
        }

        val nextState = currentState.copy(
            items = items,
            page = data.page,
            totalPage = data.pages
        )
        _surveyListResult.onNext(nextState)
    }

    @Synchronized
    private fun loadNextPage() {
        if (loadingSurveyDisposable?.isDisposed != false && hasMore) {
            val nextPage = _surveyListResult.value.page + 1
            loadingSurveyDisposable = getSurveyUseCase.getSurveys(nextPage, PAGE_SIZE)
                .doOnSubscribe {
                    if (_surveyListResult.value.items.isEmpty()) {
                        _homeState.onNext(HomeState.Loading)
                    } else {
                        _homeState.onNext(HomeState.Survey(isLoadingNext = true))
                    }
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    mergeSurveyData(it)
                    _homeState.onNext(HomeState.Survey(isLoadingNext = false))
                }, {
                    val errorMessage = transformer.transformErrorMessage(it)
                    if (_surveyListResult.value.items.isEmpty()) {
                        _homeState.onNext(HomeState.Error(errorMessage))
                    } else {
                        _homeState.onNext(HomeState.Survey(isLoadingNext = false))
                        _errorMessageObservable.onNext(errorMessage)
                    }
                })
        }
    }

    fun refreshSurveys() {
        loadingSurveyDisposable?.dispose()
        _surveyListResult.onNext(SurveyData())
        loadNextPage()
    }

    fun setCurrentPage(position: Int) {
        currentSurveyItem = position
        checkAndLoadSurveyIfNeeded()
    }

    private fun checkAndLoadSurveyIfNeeded() {
        with(_surveyListResult.value) {
            if (totalPage == null || currentSurveyItem + PREFETCH_OFFSET >= items.lastIndex) {
                loadNextPage()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadingSurveyDisposable?.dispose()
    }
}

private data class SurveyData(
    val items: List<SurveyUiModel> = emptyList(),
    val page: Int = 0,
    val totalPage: Int? = null
)