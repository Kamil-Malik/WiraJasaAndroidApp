package com.wirajasa.wirajasabisnis.feature_seller.ui.buyer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.FragmentBuyerBinding
import com.wirajasa.wirajasabisnis.feature_buyer.ui.activity.DetailServiceActivity
import com.wirajasa.wirajasabisnis.feature_buyer.ui.epoxy.ListOfServiceController
import com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel.MainViewModel
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
                    DetailServiceActivity.EXTRA_SERVICE_POST,
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
            edtSearch.addTextChangedListener(textWatcher)
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

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.getServiceByName(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            return
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}