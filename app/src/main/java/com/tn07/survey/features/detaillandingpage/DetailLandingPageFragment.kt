package com.tn07.survey.features.detaillandingpage

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.SharedElementCallback
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.MaterialContainerTransform
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
class DetailLandingPageFragment :
    BaseFragment<FragmentDetailLandingPageBinding>(FragmentDetailLandingPageBinding::inflate) {

    @Inject
    lateinit var navigator: DetailLandingPageNavigator

    private val fragmentArgs by navArgs<DetailLandingPageFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = MaterialContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
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
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onSharedElementStart(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                zoomInTransition(binding.backgroundImage)
            }
        })
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onSharedElementStart(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                zoomOutTransition(binding.backgroundImage)
            }
        })
        startPostponedEnterTransition()
    }

    private fun zoomInTransition(imageView: ImageView) {
        imageView.animate()
            .scaleX(1.5f)
            .scaleY(1.5f)
            .setDuration(150L)
            .withEndAction {
                imageView.scaleX = 1.5f
                imageView.scaleY = 1.5f
            }
            .start()
    }

    private fun zoomOutTransition(imageView: ImageView) {
        imageView.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(150L)
            .withEndAction {
                imageView.scaleX = 1f
                imageView.scaleY = 1f
            }
            .start()
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