package com.tn07.survey.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.tn07.survey.R
import com.tn07.survey.databinding.FragmentHomeBinding
import com.tn07.survey.databinding.NavHeaderHomeBinding
import com.tn07.survey.features.base.BaseFragment
import com.tn07.survey.features.base.toast
import com.tn07.survey.features.home.uimodel.HomeState
import com.tn07.survey.features.home.uimodel.LogoutResultUiModel
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import com.tn07.survey.features.home.uimodel.UserUiModel
import com.tn07.survey.features.home.view.DepthPageTransformer
import com.tn07.survey.features.home.view.SurveyAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 16, 2021 at 14:23
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var navigator: HomeNavigator

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val surveyAdapter = SurveyAdapter(this::onOpenSurveyDetail)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.user
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::bindUser)
            .addToCompositeDisposable()

        surveyAdapter.bindDataSource(viewModel.surveyListResult)
            .addToCompositeDisposable()

        viewModel.user
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::bindUser)
            .addToCompositeDisposable()

        viewModel.surveyLoadingEvents
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::bindSurveyLoadingEvent)
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
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: LogoutResultUiModel ->
                binding.loadingOverlay.visibility = View.GONE
                if (result is LogoutResultUiModel.Error) {
                    toast("Failed: $result")
                }
            }
            .addToCompositeDisposable()
    }

    private fun bindSurveyLoadingEvent(event: HomeState) {
        with(binding) {
            when (event) {
                HomeState.Loading -> {
                    contentHomePage.root.visibility = View.INVISIBLE
                    surveyLoadingLayout.root.visibility = View.VISIBLE
                    contentHomePage.swipeRefreshLayout.isRefreshing = false
                    contentHomePage.swipeRefreshLayout.isEnabled = false
                }
                HomeState.Success -> {
                    contentHomePage.root.visibility = View.VISIBLE
                    surveyLoadingLayout.root.visibility = View.GONE
                    contentHomePage.swipeRefreshLayout.isEnabled = true
                }
                HomeState.Error -> {
                    contentHomePage.root.visibility = View.INVISIBLE
                    surveyLoadingLayout.root.visibility = View.GONE
                    contentHomePage.swipeRefreshLayout.isEnabled = true
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
