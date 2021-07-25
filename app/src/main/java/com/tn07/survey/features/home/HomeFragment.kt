package com.tn07.survey.features.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.tn07.survey.R
import com.tn07.survey.databinding.FragmentHomeBinding
import com.tn07.survey.databinding.NavHeaderHomeBinding
import com.tn07.survey.features.base.BaseFragment
import com.tn07.survey.features.common.SchedulerProvider
import com.tn07.survey.features.common.applySystemBarInsets
import com.tn07.survey.features.common.toast
import com.tn07.survey.features.home.uimodel.HomeState
import com.tn07.survey.features.home.uimodel.LogoutResultUiModel
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import com.tn07.survey.features.home.uimodel.UserUiModel
import com.tn07.survey.features.home.view.DepthPageTransformer
import com.tn07.survey.features.home.view.SurveyAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 16, 2021 at 14:23
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var navigator: HomeNavigator

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private val surveyAdapter = SurveyAdapter(this::onOpenSurveyDetail)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initUser()
        viewModel.initSurveys()
        with(binding) {
            loadingOverlay.setOnClickListener { }
            navView.setNavigationItemSelectedListener {
                if (it.itemId == R.id.nav_logout) {
                    logout()
                    binding.drawerLayout.closeDrawer(binding.navView)
                    true
                } else {
                    false
                }
            }
            with(contentHomePage) {
                userAvatar.setOnClickListener {
                    binding.drawerLayout.openDrawer(binding.navView)
                }
                swipeRefreshLayout.setOnRefreshListener {
                    viewModel.refreshSurveys()
                }
                surveyViewPager.adapter = surveyAdapter
                surveyViewPager.setPageTransformer(DepthPageTransformer())
                surveyViewPager.registerOnPageChangeCallback(
                    object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            viewModel.setCurrentPage(position)
                        }

                        override fun onPageScrollStateChanged(state: Int) {
                            super.onPageScrollStateChanged(state)
                            swipeRefreshLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
                        }
                    })
                TabLayoutMediator(
                    binding.contentHomePage.pageIndicator,
                    binding.contentHomePage.surveyViewPager,
                    true,
                    false
                ) { _, _ ->
                }.attach()
            }
            errorLayout.retryButton.setOnClickListener { viewModel.refreshSurveys() }
        }
    }

    override fun handleSystemBarInsets(insets: Insets) {
        surveyAdapter.systemBarInsets = insets
        binding.contentHomePage
            .homeContentBoundary
            .updateLayoutParams<ConstraintLayout.LayoutParams> {
                applySystemBarInsets(insets)
            }

        binding.surveyLoadingLayout
            .loadingContentBoundary
            .updateLayoutParams<ConstraintLayout.LayoutParams> {
                applySystemBarInsets(insets)
            }

        binding.navView.getHeaderView(0)
            ?.let(NavHeaderHomeBinding::bind)
            ?.headerContentBoundary
            ?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                applySystemBarInsets(
                    insets,
                    initialBottomMargin = -insets.bottom // don't care about bottom insets
                )
            }
    }

    override fun onResume() {
        super.onResume()
        viewModel.user
            .observeOn(schedulerProvider.mainThread())
            .subscribe(::bindUser)
            .addToCompositeDisposable()

        surveyAdapter.bindDataSource(schedulerProvider, viewModel.surveyListResult)
            .addToCompositeDisposable()

        viewModel.user
            .observeOn(schedulerProvider.mainThread())
            .subscribe(::bindUser)
            .addToCompositeDisposable()

        viewModel.surveyLoadingEvents
            .observeOn(schedulerProvider.mainThread())
            .subscribe(::bindSurveyLoadingEvent)
            .addToCompositeDisposable()

        viewModel.errorMessageObservable
            .observeOn(schedulerProvider.mainThread())
            .subscribe(::toast)
            .addToCompositeDisposable()
    }

    private fun onOpenSurveyDetail(surveyUiModel: SurveyUiModel) {
        navigator.navigateDetailLandingPage(
            id = surveyUiModel.id,
            title = surveyUiModel.title,
            description = surveyUiModel.description,
            coverImageUrl = surveyUiModel.backgroundImageUrl
        )
    }

    private fun logout() {
        viewModel.logout()
            .doOnSubscribe {
                binding.loadingOverlay.visibility = View.VISIBLE
            }
            .observeOn(schedulerProvider.mainThread())
            .subscribe { result: LogoutResultUiModel ->
                binding.loadingOverlay.visibility = View.GONE
                if (result is LogoutResultUiModel.Success) {
                    navigator.navigateLogoutSuccess()
                } else {
                    toast("Failed: $result")
                }
            }
            .addToCompositeDisposable()
    }

    private fun bindSurveyLoadingEvent(event: HomeState) {
        with(binding) {
            when (event) {
                HomeState.Loading -> {
                    contentHomePage.root.visibility = View.GONE
                    surveyLoadingLayout.root.visibility = View.VISIBLE
                    errorLayout.root.visibility = View.GONE
                    contentHomePage.swipeRefreshLayout.isRefreshing = false
                    contentHomePage.swipeRefreshLayout.isEnabled = false
                }
                is HomeState.Survey -> {
                    contentHomePage.root.visibility = View.VISIBLE
                    surveyLoadingLayout.root.visibility = View.GONE
                    errorLayout.root.visibility = View.GONE
                    contentHomePage.swipeRefreshLayout.isEnabled = true
                    contentHomePage.loadingNextIndicator.visibility = if (event.isLoadingNext) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
                is HomeState.Error -> {
                    contentHomePage.root.visibility = View.GONE
                    surveyLoadingLayout.root.visibility = View.GONE
                    errorLayout.root.visibility = View.VISIBLE
                    errorLayout.errorMessage.text = event.errorMessage
                }
            }
            contentHomePage.dateTime.text = viewModel.todayDateTime
        }
    }

    private fun bindUser(user: UserUiModel) {
        binding.navView.getHeaderView(0)
            ?.let(NavHeaderHomeBinding::bind)
            ?.apply {
                username.text = user.email
                loadUserAvatar(user.avatar, userAvatar)
            }
        loadUserAvatar(user.avatar, binding.contentHomePage.userAvatar)
    }

    private fun loadUserAvatar(avatar: String?, imageView: ImageView) {
        Glide.with(this@HomeFragment)
            .load(avatar)
            .placeholder(R.drawable.ic_avatar_placeholder)
            .error(R.drawable.ic_avatar_placeholder)
            .into(imageView)
    }
}
