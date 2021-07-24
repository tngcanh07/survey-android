package com.tn07.survey.main.navigator

import android.app.Activity
import com.tn07.survey.features.splash.SplashFragmentDirections
import com.tn07.survey.features.splash.SplashNavigator
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 24, 2021 at 14:12
 */
class SplashNavigatorImpl @Inject constructor(
    activity: Activity
) : BaseMainNavigator(activity), SplashNavigator {

    override fun navigateToLogin() {
        navigate(SplashFragmentDirections.actionOpenLogin())
    }

    override fun navigateToHome() {
        navigate(SplashFragmentDirections.actionOpenHome())
    }
}