package com.tn07.survey.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
        binding.loadingOverlay.setOnClickListener { }
        binding.navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.nav_logout) {
                logout()
                binding.drawerLayout.closeDrawer(binding.navView)
                true
            } else {
                false
            }
        }
        binding.contentHomePage.userAvatar.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }

        viewModel.loadHomePage()
    }

    override fun onResume() {
        super.onResume()
        viewModel.homeState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::bindHomeState)
            .addToCompositeDisposable()

        viewModel.surveyListResult
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pagingData ->
                SurveyAdapter(::onOpenSurveyDetail).also {
                    it.submitData(lifecycle, pagingData)
                    binding.contentHomePage.surveyViewPager.adapter = it
                }
            }
            .addToCompositeDisposable()
        binding.contentHomePage.surveyViewPager.setPageTransformer(DepthPageTransformer())
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

    private fun bindHomeState(state: HomeState) {
        with(binding) {
            when (state) {
                HomeState.Loading -> {
                    contentHomePage.root.visibility = View.GONE
                    surveyLoadingLayout.root.visibility = View.VISIBLE
                }
                is HomeState.HomePage -> {
                    contentHomePage.root.visibility = View.VISIBLE
                    surveyLoadingLayout.root.visibility = View.GONE
                    contentHomePage.dateTime.text = state.dateTime
                    bindUser(state.user)
                }
                is HomeState.Error -> {
                    contentHomePage.root.visibility = View.GONE
                }
            }
        }
    }

    private fun bindUser(user: UserUiModel) {
        binding.navView.getHeaderView(0)?.let {
            with(NavHeaderHomeBinding.bind(it)) {
                username.text = user.email
                Glide.with(this@HomeFragment)
                    .load(user.avatar)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .error(R.drawable.ic_avatar_placeholder)
                    .into(userAvatar)
            }
        }
        Glide.with(this@HomeFragment)
            .load(user.avatar)
            .placeholder(R.drawable.ic_avatar_placeholder)
            .error(R.drawable.ic_avatar_placeholder)
            .into(binding.contentHomePage.userAvatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
