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
import com.wirajasa.wirajasabisnis.feature_seller.ui.epoxy.SellerProductEpoxyController
import com.wirajasa.wirajasabisnis.feature_seller.ui.activity.AddingServiceActivity
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
        val controller = SellerProductEpoxyController(
            onSelected = {
                Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
            }, onRetry = {

            }
        )
        binding.apply {
            fabAdd.setOnClickListener(this@DashboardFragment)
            rvListProduct.apply {
                setController(controller)
                layoutManager = LinearLayoutManager(requireContext())
                setItemSpacingDp(8)
                setHasFixedSize(true)
            }
        }
        dashboardViewModel.getAllProductsAccordingUID(uid)
            .observe(viewLifecycleOwner){
               controller.data = it
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