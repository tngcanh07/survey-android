package com.tn07.survey

import android.os.Bundle
import androidx.activity.viewModels
import com.tn07.survey.features.base.BaseActivity
import com.tn07.survey.features.home.HomeFragment
import com.tn07.survey.features.login.LoginFragment
import com.tn07.survey.features.login.LoginNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(), LoginNavigator {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (!viewModel.isLoggedIn) {
            openLoginForm()
        } else {
            openHomePage()
        }
    }

    override fun navigateLoginSuccess() {
        openHomePage()
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
}
