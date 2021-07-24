package com.tn07.survey.main.navigator

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.tn07.survey.main.MainActivity

/**
 * Created by toannguyen
 * Jul 24, 2021 at 13:56
 */
open class BaseMainNavigator(
    private val activity: Activity
) {
    private val navController: NavController
        get() = (activity as MainActivity).navController

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigate(directions: NavDirections) {
        navController.navigate(directions)
    }
}