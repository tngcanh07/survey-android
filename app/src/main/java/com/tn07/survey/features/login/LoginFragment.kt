package com.tn07.survey.features.login

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.tn07.survey.BLURRY_RADIUS
import com.tn07.survey.BLURRY_SAMPLING
import com.tn07.survey.R
import com.tn07.survey.databinding.FragmentLoginBinding
import com.tn07.survey.features.base.BaseFragment
import com.tn07.survey.features.login.uimodel.LoginResultUiModel
import com.tn07.survey.features.login.uimodel.TextFieldUiModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.wasabeef.blurry.Blurry
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 15, 2021 at 09:03
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var navigator: LoginNavigator

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()

        binding.passwordInputLayout.suffixTextView.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon!", Toast.LENGTH_SHORT).show()
        }
        binding.loginButton.setOnClickListener {
            viewModel.login(
                email = binding.emailEditText.text?.toString().orEmpty(),
                password = binding.passwordEditText.text?.toString().orEmpty()
            )
        }

        blurBackground()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loginResult
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::bindLoginResult)
            .addToCompositeDisposable()

        viewModel.loginUiModel
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                bindTextField(it.emailTextField, binding.emailEditText)
                bindTextField(it.passwordTextField, binding.passwordEditText)
                if (it.isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
            .addToCompositeDisposable()

        viewModel.loginState
            .filter { it }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                navigator.navigateLoginSuccess()
            }
            .addToCompositeDisposable()
    }

    private fun showLoading() {
        binding.emailInputLayout.isEnabled = false
        binding.passwordInputLayout.isEnabled = false
        binding.loginButton.isEnabled = false
    }

    private fun hideLoading() {
        binding.emailInputLayout.isEnabled = true
        binding.passwordInputLayout.isEnabled = true
        binding.loginButton.isEnabled = true
    }

    private fun bindTextField(
        textField: TextFieldUiModel,
        textInputEditText: TextInputEditText
    ) {
        if (textField.text != textInputEditText.text?.toString()) {
            textInputEditText.setText(textField.text)
        }
    }

    private fun bindLoginResult(uiModel: LoginResultUiModel) {
        when (uiModel) {
            is LoginResultUiModel.Error -> {
                Toast.makeText(
                    requireContext(),
                    uiModel.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
            LoginResultUiModel.Success -> {
                Toast.makeText(
                    requireContext(),
                    R.string.login_success_message,
                    Toast.LENGTH_SHORT
                ).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}