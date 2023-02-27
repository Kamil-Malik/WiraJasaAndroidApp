package com.wirajasa.wirajasabisnis.buyer.dashboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.rv_adapter.ErrorScreenAdapter
import com.wirajasa.wirajasabisnis.core.rv_adapter.LoadingScreenAdapter
import com.wirajasa.wirajasabisnis.core.rv_adapter.ServiceItemAdapter
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump
import com.wirajasa.wirajasabisnis.databinding.FragmentDashboardBuyerBinding
import com.wirajasa.wirajasabisnis.feature_buyer.ui.activity.DetailServiceActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DashboardBuyerFragment : Fragment(R.layout.fragment_dashboard_buyer), MenuProvider {

    private var _binding: FragmentDashboardBuyerBinding? = null
    private val binding: FragmentDashboardBuyerBinding get() = _binding!!
    private val viewModel: DashboardBuyerViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBuyerBinding.inflate(
            inflater,
            container,
            false
        )
        requireActivity().addMenuProvider(
            this,
            viewLifecycleOwner
        )
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val mLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvServiceItem.apply {
            layoutManager = mLayoutManager
            addItemDecoration(DividerItemDecoration(requireContext(), mLayoutManager.orientation))
            setHasFixedSize(true)
        }

        subscribeToService()
    }

    private fun subscribeToService() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.listOfService.collectLatest { services ->
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
                when (services) {
                    is NetworkResponse.GenericException -> {
                        binding.rvServiceItem.adapter = ErrorScreenAdapter(
                            errorMessage = services.cause,
                            onRetry = {
                                viewModel.getAllService()
                            }
                        )
                    }

                    is NetworkResponse.Loading -> {
                        binding.rvServiceItem.adapter = LoadingScreenAdapter(services.status)
                    }

                    is NetworkResponse.Success -> {
                        val itemAdapter = ServiceItemAdapter {service ->
                            startActivity(
                                Intent(requireContext(), DetailServiceActivity::class.java).putExtra(
                                    Dump.EXTRA_SERVICE_POST, service
                                )
                            )
                        }
                        binding.rvServiceItem.adapter = itemAdapter
                        itemAdapter.submitList(services.data)
                    }
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }

            R.id.menu_profile -> {
                findNavController().navigate(R.id.dashboard_to_profile)
                true
            }

            else -> false
        }
    }
}