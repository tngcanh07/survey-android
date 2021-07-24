package com.tn07.survey.main.navigator

import android.app.Activity
import com.tn07.survey.features.detaillandingpage.DetailLandingPageNavigator
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 24, 2021 at 14:00
 */
class DetailLandingPageNavigatorImpl @Inject constructor(
    activity: Activity
) : BaseMainNavigator(activity), DetailLandingPageNavigator {

    override fun navigateBackFromLandingPage() {
        navigateBack()
    }

    override fun navigateToSurveyDetail(surveyId: String) {

    }
}