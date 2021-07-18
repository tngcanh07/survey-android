package com.tn07.survey.features.home

/**
 * Created by toannguyen
 * Jul 18, 2021 at 14:29
 */
interface HomeNavigator {
    fun navigateDetailLandingPage(
        id: String,
        title: String,
        description: String
    )
}