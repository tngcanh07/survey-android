package com.tn07.survey.di

import com.tn07.survey.features.detaillandingpage.DetailLandingPageNavigator
import com.tn07.survey.features.forgotpassword.ForgotPasswordNavigator
import com.tn07.survey.features.home.HomeNavigator
import com.tn07.survey.features.login.LoginNavigator
import com.tn07.survey.features.splash.SplashNavigator
import com.tn07.survey.main.navigator.DetailLandingPageNavigatorImpl
import com.tn07.survey.main.navigator.ForgotPasswordNavigatorImpl
import com.tn07.survey.main.navigator.HomeNavigatorImpl
import com.tn07.survey.main.navigator.LoginNavigatorImpl
import com.tn07.survey.main.navigator.SplashNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by toannguyen
 * Jul 16, 2021 at 11:34
 */
@Module
@InstallIn(ActivityComponent::class)
interface MainActivityModule {

    @Binds
    fun bindSplashNavigator(impl: SplashNavigatorImpl): SplashNavigator

    @Binds
    fun bindLoginNavigator(impl: LoginNavigatorImpl): LoginNavigator

    @Binds
    fun bindForgotPasswordNavigator(impl: ForgotPasswordNavigatorImpl): ForgotPasswordNavigator

    @Binds
    fun bindHomeNavigator(impl: HomeNavigatorImpl): HomeNavigator

    @Binds
    fun bindDetailLandingPageNavigator(impl: DetailLandingPageNavigatorImpl): DetailLandingPageNavigator
}