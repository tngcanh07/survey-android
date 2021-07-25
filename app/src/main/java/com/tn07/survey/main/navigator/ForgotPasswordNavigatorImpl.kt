package com.tn07.survey.main.navigator

import android.app.Activity
import com.tn07.survey.features.forgotpassword.ForgotPasswordNavigator
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 25, 2021 at 14:54
 */
class ForgotPasswordNavigatorImpl @Inject constructor(
    activity: Activity
) : BaseMainNavigator(activity), ForgotPasswordNavigator