package com.wirajasa.wirajasabisnis.feature_seller.ui.buyer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.FragmentBuyerBinding
import com.wirajasa.wirajasabisnis.feature_buyer.ui.activity.DetailServiceActivity
import com.wirajasa.wirajasabisnis.feature_buyer.ui.epoxy.ListOfServiceController
import com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel.MainViewModel
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump.EXTRA_SERVICE_POST
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyerFragment : Fragment() {

    private var _binding: FragmentBuyerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var controller: ListOfServiceController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllService()
        controller = ListOfServiceController(onSelected = {
            startActivity(
                Intent(activity, DetailServiceActivity::class.java).putExtra(
                    EXTRA_SERVICE_POST,
                    it
                )
            )
        }, onRetry = {
            viewModel.getAllService()
        })

        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.textview, resources.getStringArray(R.array.province))

        binding.apply {
            edtSearch.setAdapter(arrayAdapter)
            edtSearch.addTextChangedListener {
                viewModel.getServiceByName(it.toString())
            }
            epoxyService.apply {
                setController(controller)
                layoutManager = LinearLayoutManager(requireContext())
                setItemSpacingDp(8)
                setHasFixedSize(true)
            }
        }
        subscribe()
    }

    private fun subscribe() {
        viewModel.listOfService.observe(viewLifecycleOwner) {
            controller.data = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}