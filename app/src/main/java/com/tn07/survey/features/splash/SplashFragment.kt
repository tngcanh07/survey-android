package com.tn07.survey.features.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tn07.survey.databinding.FragmentSplashBinding
import com.tn07.survey.features.base.BaseFragment
import com.tn07.survey.features.common.fadeInAnimation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 24, 2021 at 00:19
 */
private const val SPLASH_SCREEN_DURATION = 1250

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    @Inject
    lateinit var navigator: SplashNavigator

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.splashBranchIcon.fadeInAnimation(
            duration = SPLASH_SCREEN_DURATION,
            onAnimationEnd = {
                processToSurveyApp()
            },
            onAnimationCancel = {
                processToSurveyApp()
            }
        )
    }

    private fun processToSurveyApp() {
        if (viewModel.isLoggedIn) {
            navigator.navigateToHome()
        } else {
            navigator.navigateToLogin()
        }
    }
}