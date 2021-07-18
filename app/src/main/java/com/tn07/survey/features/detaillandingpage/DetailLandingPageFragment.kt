package com.tn07.survey.features.detaillandingpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tn07.survey.databinding.FragmentDetailLandingPageBinding
import com.tn07.survey.features.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 18, 2021 at 14:21
 */
@AndroidEntryPoint
class DetailLandingPageFragment : BaseFragment() {

    @Inject
    lateinit var navigator: DetailLandingPageNavigator

    private var _binding: FragmentDetailLandingPageBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var surveyId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailLandingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            navigator.navigateBackFromLandingPage()
        }

        binding.startSurveyButton.setOnClickListener {
            navigator.navigateToSurveyDetail(surveyId)
        }

    }
}