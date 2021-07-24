package com.tn07.survey.main.navigator

import android.app.Activity
import com.tn07.survey.features.login.LoginFragmentDirections
import com.tn07.survey.features.login.LoginNavigator
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 24, 2021 at 14:00
 */
class LoginNavigatorImpl @Inject constructor(
    activity: Activity
) : BaseMainNavigator(activity), LoginNavigator {

    override fun navigateLoginSuccess() {
        navigate(LoginFragmentDirections.actionLoginOpenHome())
    }
}