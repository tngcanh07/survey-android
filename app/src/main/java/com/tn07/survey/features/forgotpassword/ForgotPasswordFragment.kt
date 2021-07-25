package com.tn07.survey.features.forgotpassword

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.tn07.survey.BLURRY_RADIUS
import com.tn07.survey.BLURRY_SAMPLING
import com.tn07.survey.DEBOUNCE_TEXT_CHANGE_EVENT
import com.tn07.survey.R
import com.tn07.survey.databinding.FragmentForgotPasswordBinding
import com.tn07.survey.features.base.BaseFragment
import com.tn07.survey.features.common.SchedulerProvider
import com.tn07.survey.features.common.applySystemBarInsets
import com.tn07.survey.features.common.toast
import com.tn07.survey.features.forgotpassword.uimodel.ForgotPasswordUiModel
import com.tn07.survey.features.forgotpassword.uimodel.ResetPasswordResult
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 25, 2021 at 14:37
 */

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {

    @Inject
    lateinit var navigator: ForgotPasswordNavigator

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
        with(binding) {
            ResourcesCompat.getDrawable(resources, R.drawable.app_background, context?.theme)
                .let { it as? BitmapDrawable }
                ?.let {
                    Blurry.with(requireContext())
                        .radius(BLURRY_RADIUS)
                        .sampling(BLURRY_SAMPLING)
                        .from(it.bitmap)
                        .into(backgroundImage)
                }

            backButton.setOnClickListener {
                navigator.navigateBack()
            }

            resetPasswordButton.setOnClickListener {
                viewModel.resetPassword()
            }

            emailEditText.afterTextChangeEvents()
                .debounce(DEBOUNCE_TEXT_CHANGE_EVENT, TimeUnit.MILLISECONDS)
                .subscribe {
                    viewModel.setEmail(it.editable?.toString().orEmpty())
                }
                .addToCompositeDisposable()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.forgotPasswordUiModel
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe(::bindForgotPasswordUiModel)
            .addToCompositeDisposable()

        viewModel.resetPasswordResult
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe(::bindResetPasswordResult)
            .addToCompositeDisposable()

        binding.emailEditText.afterTextChangeEvents()
            .debounce(DEBOUNCE_TEXT_CHANGE_EVENT, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.setEmail(it.editable?.toString().orEmpty())
            }
            .addToCompositeDisposable()
    }

    override fun handleSystemBarInsets(insets: Insets) {
        super.handleSystemBarInsets(insets)
        binding.forgotPasswordContentBoundary.updateLayoutParams<ConstraintLayout.LayoutParams> {
            applySystemBarInsets(insets)
        }
    }

    private fun bindResetPasswordResult(result: ResetPasswordResult) {
        if (result is ResetPasswordResult.Error) {
            toast(result.errorMessage)
        } else {
            toast(R.string.reset_password_success_message, Toast.LENGTH_LONG)
            navigator.navigateBack()
        }
    }

    private fun bindForgotPasswordUiModel(uiModel: ForgotPasswordUiModel) {
        with(binding) {
            if (!emailEditText.isFocused && uiModel.email != emailEditText.text?.toString()) {
                emailEditText.setText(uiModel.email)
            }

            emailInputLayout.error = uiModel.emailErrorMessage
            if (uiModel.emailErrorMessage.isNullOrEmpty()) {
                emailInputLayout.isErrorEnabled = false
            }

            if (uiModel.isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            emailInputLayout.isEnabled = false
            resetPasswordButton.apply {
                isEnabled = false
                setText(R.string.reset_password_processing_button)
            }
        }
    }

    private fun hideLoading() {
        with(binding) {
            emailInputLayout.isEnabled = true
            resetPasswordButton.apply {
                isEnabled = true
                setText(R.string.reset_password_button)
            }
        }
    }
}