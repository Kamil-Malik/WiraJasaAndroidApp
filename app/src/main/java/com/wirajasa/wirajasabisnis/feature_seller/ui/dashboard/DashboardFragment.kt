package com.wirajasa.wirajasabisnis.feature_seller.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.FragmentSellerDashboardBinding
import com.wirajasa.wirajasabisnis.presentation.service.AddingServiceActivity
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSellerDashboardBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel by viewModels<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = dashboardViewModel.getProfile().uid
        dashboardViewModel.getAllProductsAccordingUID(uid)
            .observe(viewLifecycleOwner){
                when(it){
                    is NetworkResponse.GenericException -> Toast.makeText(
                        activity,
                        it.cause.toString(),
                        Toast.LENGTH_SHORT
                    ).show().also{ showLoading(false) }
                    is NetworkResponse.Loading -> showLoading(true)
                    is NetworkResponse.Success -> {
                        showLoading(false)
                        binding.rvListProduct.setHasFixedSize(true)
                        val layoutManager = LinearLayoutManager(activity)
                        binding.rvListProduct.layoutManager = layoutManager
                        val adapter = DashboardAdapter(requireContext(),it.data)
                        binding.rvListProduct.adapter = adapter
                    }
                }
            }
        binding.fabAdd.setOnClickListener(this)
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.tvLoading.visibility = View.VISIBLE
            binding.pbDashboard.visibility = View.VISIBLE
        }else{
            binding.tvLoading.visibility = View.GONE
            binding.pbDashboard.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add -> {
                activity?.startActivity(Intent(activity, AddingServiceActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}