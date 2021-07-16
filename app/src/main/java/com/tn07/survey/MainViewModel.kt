package com.tn07.survey

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.usecases.GetTokenUseCase
import com.tn07.survey.features.base.BaseViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 16, 2021 at 14:00
 */
class MainViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase
) : BaseViewModel() {

    val isLoggedIn: Boolean
        get() = getTokenUseCase.getToken() is AccessToken

    val loginStatusObservable: Observable<Boolean>
        get() = getTokenUseCase.getTokenObservable()
            .map { it is AccessToken }
            .distinctUntilChanged()
}