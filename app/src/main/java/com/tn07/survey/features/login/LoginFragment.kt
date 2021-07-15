package com.tn07.survey.features.login

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tn07.survey.R
import com.tn07.survey.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry

/**
 * Created by toannguyen
 * Jul 15, 2021 at 09:03
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()

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
        binding.passwordInputLayout.suffixTextView.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon!", Toast.LENGTH_SHORT).show()
        }

        blurBackground()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun blurBackground() {
        val imageView = binding.backgroundImage
        ResourcesCompat.getDrawable(resources, R.drawable.app_background, context?.theme)
            .let { it as? BitmapDrawable }
            ?.let {
                Blurry.with(requireContext())
                    .radius(25)
                    .sampling(2)
                    .from(it.bitmap)
                    .into(imageView)
            }
    }
}