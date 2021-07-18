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
import com.tn07.survey.features.home.uimodel.UserUiModel
import com.tn07.survey.features.home.view.DepthPageTransformer
import com.tn07.survey.features.home.view.SurveyAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

/**
 * Created by toannguyen
 * Jul 16, 2021 at 14:23
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModels()

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
                SurveyAdapter().also {
                    it.submitData(lifecycle, pagingData)
                    binding.contentHomePage.surveyViewPager.adapter = it
                }
            }
        binding.contentHomePage.surveyViewPager.setPageTransformer(DepthPageTransformer())
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
        when (state) {
            HomeState.Loading -> {
                binding.contentHomePage.root.visibility = View.GONE
            }
            is HomeState.HomePage -> {
                binding.contentHomePage.root.visibility = View.VISIBLE
                binding.contentHomePage.dateTime.text = state.dateTime
                bindUser(state.user)
            }
            is Error -> {
                binding.contentHomePage.root.visibility = View.GONE
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
