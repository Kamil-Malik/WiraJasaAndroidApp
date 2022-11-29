package com.wirajasa.wirajasabisnis.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.adapter.ProductAdapter
import com.wirajasa.wirajasabisnis.data.model.Product
import com.wirajasa.wirajasabisnis.databinding.ActivityBuyerDashboardBinding

class BuyerDashboardActivity : AppCompatActivity() {

    private var _binding : ActivityBuyerDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var product: Product
    private lateinit var viewModel: BuyerDashboardViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBuyerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(BuyerDashboardViewModel::class.java)
        productAdapter = ProductAdapter()

        binding.rvListProduct.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(this@BuyerDashboardActivity, LinearLayoutManager.VERTICAL, false)

        }
    }
}