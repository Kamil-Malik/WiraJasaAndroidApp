package com.wirajasa.wirajasabisnis.feature_admin.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.databinding.ActivityAdminBinding
import com.wirajasa.wirajasabisnis.feature_admin.ui.contract.FormResultContract
import com.wirajasa.wirajasabisnis.feature_admin.ui.epoxy.AdminApplicationFormController
import com.wirajasa.wirajasabisnis.feature_admin.ui.viewmodel.AdminViewModel
import com.wirajasa.wirajasabisnis.feature_auth.ui.activity.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private lateinit var controller: AdminApplicationFormController
    private val viewModel by viewModels<AdminViewModel>()
    private val contract = registerForActivityResult(FormResultContract()) {
        if(it) viewModel.getApplicationList()
    }
    private val binding by lazy {
        ActivityAdminBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.adminToolbar)
        supportActionBar?.title = ""
        setContentView(binding.root)

        controller = AdminApplicationFormController(onSelected = {
            contract.launch(it)
        }, onRetry = {
            viewModel.getApplicationList()
        })
        binding.apply {
            rvApplicationForm.setController(controller)
            rvApplicationForm.layoutManager = LinearLayoutManager(this@AdminActivity)
            rvApplicationForm.setItemSpacingDp(8)
        }
        subscribe()
    }

    private fun subscribe() {
        viewModel.applicationList.observe(this) {
            controller.status = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                Firebase.auth.signOut()
                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
        return true
    }
}