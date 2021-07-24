package com.tn07.survey.features.splash

import com.tn07.survey.domain.entities.AccessToken
import com.tn07.survey.domain.usecases.GetTokenUseCase
import com.tn07.survey.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 24, 2021 at 12:57
 */

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase
) : BaseViewModel() {

    val isLoggedIn: Boolean
        get() = getTokenUseCase.getToken() is AccessToken
}