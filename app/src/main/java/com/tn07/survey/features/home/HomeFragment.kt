package com.tn07.survey.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tn07.survey.R
import com.tn07.survey.databinding.FragmentHomeBinding
import com.tn07.survey.databinding.NavHeaderHomeBinding
import com.tn07.survey.features.base.BaseFragment
import com.tn07.survey.features.home.uimodel.HomeState
import com.tn07.survey.features.home.uimodel.UserUiModel
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
        binding.navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.nav_logout) {
                viewModel.logout()
                binding.drawerLayout.closeDrawer(binding.navView)
                true
            } else {
                false
            }
        }
        binding.loadingOverlay.setOnClickListener { }

        viewModel.loadHomePage()
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadingState
            .observeOn(AndroidSchedulers.mainThread())
            .map { if (it) View.VISIBLE else View.GONE }
            .subscribe {
                binding.loadingOverlay.visibility = it
            }
            .addToCompositeDisposable()

        viewModel.homeState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::bindHomeState)
            .addToCompositeDisposable()
    }

    private fun bindHomeState(state: HomeState) {
        when (state) {
            HomeState.Loading -> {

            }
            is HomeState.HomePage -> {
                bindUser(state.user)
            }
            is Error -> {

            }
        }
    }

    private fun bindUser(user: UserUiModel) {
        binding.navView.getHeaderView(0)?.let {
            with(NavHeaderHomeBinding.bind(it)) {
                username.text = user.email
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}