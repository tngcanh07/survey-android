package com.tn07.survey.features.login

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.tn07.survey.BLURRY_RADIUS
import com.tn07.survey.BLURRY_SAMPLING
import com.tn07.survey.DEBOUNCE_TEXT_CHANGE_EVENT
import com.tn07.survey.R
import com.tn07.survey.databinding.FragmentLoginBinding
import com.tn07.survey.features.base.BaseFragment
import com.tn07.survey.features.common.SchedulerProvider
import com.tn07.survey.features.common.applySystemBarInsets
import com.tn07.survey.features.common.toast
import com.tn07.survey.features.login.uimodel.LoginResultUiModel
import com.tn07.survey.features.login.uimodel.TextFieldUiModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 15, 2021 at 09:03
 */

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var navigator: LoginNavigator

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()

        binding.passwordInputLayout.suffixTextView.setOnClickListener {
            navigator.navigateForgotPassword()
        }
        binding.emailEditText.doAfterTextChanged {
            with(binding.emailInputLayout) {
                if (error?.isNotEmpty() == true) {
                    error = null
                    isErrorEnabled = false
                }
            }
        }
        binding.passwordEditText.doAfterTextChanged {
            with(binding.passwordInputLayout) {
                if (error?.isNotEmpty() == true) {
                    error = null
                    isErrorEnabled = false
                }
            }
        }
        binding.loginButton.setOnClickListener {
            viewModel.login()
        }
        blurBackground()
    }

    override fun handleSystemBarInsets(insets: Insets) {
        super.handleSystemBarInsets(insets)
        binding.loginContentBoundary.updateLayoutParams<ConstraintLayout.LayoutParams> {
            applySystemBarInsets(insets)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loginResult
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe(::bindLoginResult)
            .addToCompositeDisposable()

        viewModel.loginUiModel
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe {
                bindTextField(it.emailTextField, binding.emailInputLayout)
                bindTextField(it.passwordTextField, binding.passwordInputLayout)
                if (it.isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
            .addToCompositeDisposable()

        viewModel.loginState
            .filter { it }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe {
                navigator.navigateLoginSuccess()
            }
            .addToCompositeDisposable()


        binding.emailEditText.afterTextChangeEvents()
            .debounce(DEBOUNCE_TEXT_CHANGE_EVENT, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.setEmail(it.editable?.toString().orEmpty())
            }
            .addToCompositeDisposable()

        binding.passwordEditText.afterTextChangeEvents()
            .debounce(DEBOUNCE_TEXT_CHANGE_EVENT, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.setPassword(it.editable?.toString().orEmpty())
            }
            .addToCompositeDisposable()
    }

    private fun showLoading() {
        binding.emailInputLayout.isEnabled = false
        binding.passwordInputLayout.isEnabled = false
        binding.loginButton.apply {
            isEnabled = false
            setText(R.string.login_processing_button)
        }
    }

    private fun hideLoading() {
        binding.emailInputLayout.isEnabled = true
        binding.passwordInputLayout.isEnabled = true
        binding.loginButton.apply {
            isEnabled = true
            setText(R.string.login_button)
        }
    }

    private fun bindTextField(
        textField: TextFieldUiModel,
        textInputLayout: TextInputLayout
    ) {
        textInputLayout.editText?.let { editText ->
            if (textField.text != editText.text?.toString() && !editText.isFocused) {
                editText.setText(textField.text)
            }
        }

        textInputLayout.error = textField.error
        if (textField.error.isNullOrEmpty()) {
            textInputLayout.isErrorEnabled = false
        }
    }

    private fun bindLoginResult(uiModel: LoginResultUiModel) {
        when (uiModel) {
            is LoginResultUiModel.Error -> {
                toast(uiModel.errorMessage)
            }
            LoginResultUiModel.Success -> {
                toast(R.string.login_success_message)
            }
        }
    }

    private fun blurBackground() {
        val imageView = binding.backgroundImage
        ResourcesCompat.getDrawable(resources, R.drawable.app_background, context?.theme)
            .let { it as? BitmapDrawable }
            ?.let {
                Blurry.with(requireContext())
                    .radius(BLURRY_RADIUS)
                    .sampling(BLURRY_SAMPLING)
                    .from(it.bitmap)
                    .into(imageView)
            }
    }
}