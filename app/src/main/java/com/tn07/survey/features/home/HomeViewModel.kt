package com.tn07.survey.features.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.tn07.survey.domain.usecases.GetSurveyUseCase
import com.tn07.survey.domain.usecases.GetUserUseCase
import com.tn07.survey.domain.usecases.LogoutUseCase
import com.tn07.survey.features.base.BaseViewModel
import com.tn07.survey.features.home.pagingsource.SurveyPagingSource
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

    private val _homeState = BehaviorSubject.create<HomeState>()
    val homeState: Observable<HomeState>
        get() = _homeState.distinctUntilChanged()

    private var currentSurveyPagingSource: SurveyPagingSource? = null

    private val _surveyListResult: BehaviorSubject<PagingData<SurveyUiModel>> =
        BehaviorSubject.create()
    val surveyListResult: Flowable<PagingData<SurveyUiModel>>
        get() = _surveyListResult.toFlowable(BackpressureStrategy.LATEST)

    private val surveyPagingConfig: PagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        maxSize = MAX_CAPACITY,
        initialLoadSize = PAGE_SIZE,
        prefetchDistance = PREFETCH_DISTANCE
    )

    init {
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

        Pager(surveyPagingConfig) {
            println("new dataSource")
            SurveyPagingSource(
                getSurveyUseCase = getSurveyUseCase,
                startPageIndex = START_PAGE_INDEX
            ).also {
                currentSurveyPagingSource = it
            }
        }
            .flowable
            .cachedIn(viewModelScope)
            .map {
                it.map(transformer::transformSurvey)
            }
            .subscribe(_surveyListResult::onNext)
            .addToCompositeDisposable()
    }

    fun logout(): Single<LogoutResultUiModel> {
        return logoutUseCase.logout()
            .andThen(Single.just<LogoutResultUiModel>(LogoutResultUiModel.Success))
            .onErrorResumeNext {
                Single.just(LogoutResultUiModel.Error)
            }
    }

    companion object {
        const val START_PAGE_INDEX = 1
        const val PAGE_SIZE = 5
        const val PREFETCH_DISTANCE = 1
        const val MAX_CAPACITY = 20
    }
}