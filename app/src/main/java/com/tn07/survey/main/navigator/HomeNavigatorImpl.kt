package com.tn07.survey.main.navigator

import android.app.Activity
import android.widget.TextView
import com.tn07.survey.features.home.HomeFragmentDirections
import com.tn07.survey.features.home.HomeNavigator
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 24, 2021 at 14:06
 */
class HomeNavigatorImpl @Inject constructor(
    activity: Activity
) : BaseMainNavigator(activity), HomeNavigator {
    
    override fun navigateDetailLandingPage(
        id: String,
        title: String,
        description: String,
        coverImageUrl: String,
        textView: TextView
    ) {
        navigate(
            HomeFragmentDirections.actionOpenDetailLandingPage(
                surveyId = id,
                surveyTitle = title,
                surveyDescription = description,
                surveyCoverImageUrl = coverImageUrl
            )
        )
    }

    override fun navigateLogoutSuccess() {
        navigate(HomeFragmentDirections.actionOpenLogin())
    }
}