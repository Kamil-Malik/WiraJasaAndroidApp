package com.wirajasa.wirajasabisnis.feature_buyer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityMainBinding
import com.wirajasa.wirajasabisnis.feature_buyer.ui.epoxy.ListOfServiceController
import com.wirajasa.wirajasabisnis.feature_buyer.ui.viewmodel.MainViewModel
import com.wirajasa.wirajasabisnis.utility.constant.Dump.EXTRA_SERVICE_POST
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var controller: ListOfServiceController
    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarMain)
        setContentView(binding.root)
        viewModel.getAllService()

        val currentUser = viewModel.getUser()
        if (!currentUser.username.contains("Guest")) binding.tvGreetUser.text =
            "Hi, ${currentUser.username}"

        controller = ListOfServiceController(onSelected = {
            startActivity(
                Intent(this, DetailServiceActivity::class.java).putExtra(
                    EXTRA_SERVICE_POST, it
                )
            )
        }, onRetry = {
            viewModel.getAllService()
        })

        val arrayAdapter =
            ArrayAdapter(this, R.layout.textview, resources.getStringArray(R.array.province))

        binding.apply {
            edtSearch.setAdapter(arrayAdapter)
            edtSearch.addTextChangedListener {
                viewModel.getServiceByName(it.toString())
            }
            epoxyService.apply {
                setController(controller)
                layoutManager = LinearLayoutManager(this@MainActivity)
                setItemSpacingDp(8)
                setHasFixedSize(true)
            }
        }
        subscribe()
    }

    private fun subscribe() {
        viewModel.listOfService.observe(this) {
            controller.data = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_profile -> startActivity(Intent(this, ProfileActivity::class.java))
        }
        return true
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
}