package com.tn07.survey

import android.os.Bundle
import androidx.activity.viewModels
import com.tn07.survey.features.base.BaseActivity
import com.tn07.survey.features.detaillandingpage.DetailLandingPageFragment
import com.tn07.survey.features.detaillandingpage.DetailLandingPageNavigator
import com.tn07.survey.features.home.HomeFragment
import com.tn07.survey.features.home.HomeNavigator
import com.tn07.survey.features.login.LoginFragment
import com.tn07.survey.features.login.LoginNavigator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class MainActivity : BaseActivity(), LoginNavigator, HomeNavigator, DetailLandingPageNavigator {

    private val viewModel by viewModels<MainViewModel>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (!viewModel.isLoggedIn) {
            openLoginForm()
        } else {
            openHomePage()
        }

        viewModel.loginStatusObservable
            .subscribeOn(AndroidSchedulers.mainThread())
            .skip(1)
            .scan(::isUserLoggedOut)
            .filter { it }
            .subscribe {
                openLoginForm()
            }
            .let(compositeDisposable::add)
    }

    private fun isUserLoggedOut(currentStatus: Boolean, nextStatus: Boolean): Boolean {
        return currentStatus && !nextStatus
    }

    override fun navigateLoginSuccess() {
        openHomePage()
    }

    override fun navigateDetailLandingPage(id: String, title: String, description: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailLandingPageFragment())
            .commit()
    }

    override fun navigateBackFromLandingPage() {
        openHomePage()
    }

    override fun navigateToSurveyDetail(surveyId: String) {
        TODO("Not yet implemented")
    }

    private fun openLoginForm() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, LoginFragment())
            .commit()
    }

    private fun openHomePage() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
