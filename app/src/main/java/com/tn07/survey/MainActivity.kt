package com.tn07.survey

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tn07.survey.features.base.BaseActivity
import com.tn07.survey.features.common.SchedulerProvider
import com.tn07.survey.features.detaillandingpage.DetailLandingPageFragmentArgs
import com.tn07.survey.features.detaillandingpage.DetailLandingPageNavigator
import com.tn07.survey.features.home.HomeNavigator
import com.tn07.survey.features.login.LoginNavigator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), LoginNavigator, HomeNavigator, DetailLandingPageNavigator {

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private val viewModel by viewModels<MainViewModel>()
    private val compositeDisposable = CompositeDisposable()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.main_activity)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        if (!viewModel.isLoggedIn) {
            startLoginNavigation()
        } else {
            startSurveyNavigation()
        }

        viewModel.loginStatusObservable
            .skip(1)
            .filter { !it }
            .observeOn(schedulerProvider.mainThread())
            .subscribe {
                startLoginNavigation()
            }
            .let(compositeDisposable::add)

    }

    override fun navigateLoginSuccess() {
        startSurveyNavigation()
    }

    override fun navigateDetailLandingPage(
        id: String,
        title: String,
        description: String,
        coverImageUrl: String
    ) {
        val args = DetailLandingPageFragmentArgs(
            surveyId = id,
            surveyTitle = title,
            surveyDescription = description,
            surveyCoverImageUrl = coverImageUrl
        )
        navController.navigate(R.id.action_open_detail_landing_page, args.toBundle())
    }

    override fun navigateBackFromLandingPage() {
        navController.popBackStack()
    }

    override fun navigateToSurveyDetail(surveyId: String) {
        // TODO
    }

    private fun startLoginNavigation() {
        navController.setGraph(R.navigation.login_navigation)
    }

    private fun startSurveyNavigation() {
        navController.setGraph(R.navigation.survey_navigation)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
