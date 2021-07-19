package com.tn07.survey.features.detaillandingpage

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tn07.survey.databinding.FragmentDetailLandingPageBinding
import com.tn07.survey.features.base.BaseFragment
import com.tn07.survey.features.common.applySystemBarInsets
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

    private val fragmentArgs by navArgs<DetailLandingPageFragmentArgs>()

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
        with(binding) {
            backButton.setOnClickListener {
                navigator.navigateBackFromLandingPage()
            }

            startSurveyButton.setOnClickListener {
                navigator.navigateToSurveyDetail(fragmentArgs.surveyId)
            }

            surveyTitle.text = fragmentArgs.surveyTitle
            surveyDescription.text = fragmentArgs.surveyDescription
            Glide.with(this@DetailLandingPageFragment)
                .load(fragmentArgs.surveyCoverImageUrl)
                .addListener(listener)
                .into(backgroundImage)
        }
    }

    override fun handleSystemBarInsets(insets: Insets) {
        super.handleSystemBarInsets(insets)
        binding.landingPageContentBoundary.updateLayoutParams<ConstraintLayout.LayoutParams> {
            applySystemBarInsets(insets)
        }
    }

    private val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }
    }

}