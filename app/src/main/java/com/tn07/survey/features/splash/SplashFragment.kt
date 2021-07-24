package com.tn07.survey.features.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tn07.survey.databinding.FragmentSplashBinding
import com.tn07.survey.features.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 24, 2021 at 00:19
 */
@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    @Inject
    lateinit var navigator: SplashNavigator

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.isLoggedIn) {
            navigator.navigateToHome()
        } else {
            navigator.navigateToLogin()
        }
    }
}