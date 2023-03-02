package com.wirajasa.wirajasabisnis.role_admin.dashboard

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.rv_adapter.ErrorScreenAdapter
import com.wirajasa.wirajasabisnis.core.rv_adapter.LoadingScreenAdapter
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.databinding.ActivityAdminBinding
import com.wirajasa.wirajasabisnis.feature_auth.ui.activity.LoginActivity
import com.wirajasa.wirajasabisnis.role_admin.adapter.FormAdapter
import com.wirajasa.wirajasabisnis.role_admin.contract.FormResultContract
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdministrationActivity : AppCompatActivity() {

    private val binding: ActivityAdminBinding by viewBinding()
    private val viewModel: AdminViewModel by viewModels()
    val contract = registerForActivityResult(FormResultContract()) { updated ->
        if (updated) viewModel.getApplicationList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        viewModel.applicationList.observe(this) { result ->
            TransitionManager.beginDelayedTransition(binding.root)
            when (result) {
                is NetworkResponse.GenericException -> {
                    binding.rvApplicationForm.adapter =
                        ErrorScreenAdapter(errorMessage = result.cause) {
                            viewModel.getApplicationList()
                        }
                }

                is NetworkResponse.Loading -> {
                    binding.rvApplicationForm.adapter = LoadingScreenAdapter(result.status)
                }

                is NetworkResponse.Success -> {
                    val mLayoutManager = LinearLayoutManager(this)
                    val formAdapter = FormAdapter { sellerApplication ->
                        contract.launch(sellerApplication)
                    }
                    binding.rvApplicationForm.apply {
                        adapter = formAdapter
                        layoutManager = mLayoutManager
                        addItemDecoration(
                            DividerItemDecoration(
                                this@AdministrationActivity,
                                mLayoutManager.orientation
                            )
                        )
                        setHasFixedSize(true)
                    }
                    formAdapter.submitList(result.data)
                }
            }
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