package com.tn07.survey.features.home

import com.tn07.survey.domain.entities.Pageable
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.usecases.GetSurveyUseCase
import com.tn07.survey.domain.usecases.GetUserUseCase
import com.tn07.survey.domain.usecases.LogoutUseCase
import com.tn07.survey.features.base.BaseViewModel
import com.tn07.survey.features.home.transformer.HomeTransformer
import com.tn07.survey.features.home.uimodel.LogoutResultUiModel
import com.tn07.survey.features.home.uimodel.SurveyLoadingState
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import com.tn07.survey.features.home.uimodel.UserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
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

    private val _surveyLoadingState = BehaviorSubject.create<SurveyLoadingState>()
    val surveyLoadingState: Observable<SurveyLoadingState>
        get() = _surveyLoadingState.distinctUntilChanged()

    private val _user = BehaviorSubject.create<UserUiModel>()
    val user: Observable<UserUiModel>
        get() = _user.distinctUntilChanged()

    private val _surveyListResult = BehaviorSubject.createDefault(SurveyData())
    val surveyListResult: Flowable<List<SurveyUiModel>>
        get() = _surveyListResult.toFlowable(BackpressureStrategy.LATEST)
            .map(SurveyData::items)
    private var hasMore: Boolean = true

    private var loadingSurveyDisposable: Disposable? = null

    var currentSurveyItem: Int = 0
        private set

    val todayDateTime: String
        get() = transformer.todayDateTime

    init {
        loadUser()
        loadNextPage()
    }

    private fun loadUser() {
        getUserUseCase.getUserObservable()
            .map(transformer::transformUser)
            .subscribeOn(Schedulers.io())
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

    private fun onSurveyLoaded(data: Pageable<Survey>) {
        val currentState = _surveyListResult.value
        val nextState = currentState.copy(
            items = currentState.items + data.items.map(transformer::transformSurvey),
            page = data.page,
            totalPage = data.pages
        )
        hasMore = data.page < data.pages
        _surveyListResult.onNext(nextState)
    }

    private fun onSurveyLoadFailed(throwable: Throwable) {

    }

    @Synchronized
    private fun loadNextPage() {
        if (loadingSurveyDisposable?.isDisposed != false) {
            val currentData = _surveyListResult.value
            val nextPage = currentData.page + 1
            loadingSurveyDisposable = getSurveyUseCase.getSurveys(nextPage, PAGE_SIZE)
                .doOnSubscribe {
                    _surveyLoadingState.onNext(SurveyLoadingState.Loading(nextPage))
                }
                .subscribe({
                    onSurveyLoaded(it)
                    _surveyLoadingState.onNext(SurveyLoadingState.LoadSuccess(nextPage))
                }, {
                    onSurveyLoadFailed(it)
                    _surveyLoadingState.onNext(SurveyLoadingState.LoadError(nextPage))
                })
        }
    }

    fun refreshSurveys() {
        loadingSurveyDisposable?.dispose()
        _surveyListResult.onNext(SurveyData())
        hasMore = true
        loadNextPage()
    }

    fun setCurrentPage(position: Int) {
        currentSurveyItem = position
        if (position + PREFETCH_OFFSET >= _surveyListResult.value.items.lastIndex) {
            loadNextPage()
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadingSurveyDisposable?.dispose()
    }

    companion object {
        private const val PAGE_SIZE = 5
        private const val PREFETCH_OFFSET = 1
    }
}

private data class SurveyData(
    val items: List<SurveyUiModel> = emptyList(),
    val page: Int = 0,
    val totalPage: Int? = null

)