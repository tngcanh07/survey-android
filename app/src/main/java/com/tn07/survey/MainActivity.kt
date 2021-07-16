package com.tn07.survey

import android.os.Bundle
import androidx.activity.viewModels
import com.tn07.survey.features.base.BaseActivity
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
            supportFragmentManager.beginTransaction()
                .add(R.id.container, LoginFragment())
                .commit()
        }
    }
    
    override fun navigateLoginSuccess() {
        supportFragmentManager.findFragmentById(R.id.container)
            ?.let { it as? LoginFragment }
            ?.let {
                supportFragmentManager.beginTransaction()
                    .remove(it)
                    .commit()
            }

    }
}
