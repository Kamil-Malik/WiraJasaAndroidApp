package com.wirajasa.wirajasabisnis.feature_admin.ui.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.VERSION
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.databinding.ActivityDetailFormBinding
import com.wirajasa.wirajasabisnis.feature_admin.domain.repository_impl.AdminRepositoryImpl
import com.wirajasa.wirajasabisnis.feature_admin.ui.viewmodel.DetailFormViewModel
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFormActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<DetailFormViewModel>()
    private lateinit var form: SellerApplication
    private val binding by lazy {
        ActivityDetailFormBinding.inflate(layoutInflater)
    }
    private val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            binding.circleLoading.visibility = View.GONE
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            binding.circleLoading.visibility = View.GONE
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.adminToolbar)
        supportActionBar?.title = ""
        window?.statusBarColor = getColor(R.color.dark_blue)
        setContentView(binding.root)
        form = if (VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("FORM", SellerApplication::class.java)
        } else {
            intent.getParcelableExtra("FORM")
        } as SellerApplication
        binding.apply {
            Glide.with(this@DetailFormActivity)
                .load(form.photoID)
                .listener(listener)
                .into(ivIdCard)

            tvApplicationId.text = getString(R.string.admin_application_form_id, form.applicationId)
            tvApplicationCreator.text = getString(R.string.admin_applicant_name, form.fullName)
            tvApplicationProvince.text = getString(R.string.admin_tv_province, form.province)
            tvApplicationAddress.text = getString(R.string.admin_tv_address, form.address)
            btnCallSeller.setOnClickListener(this@DetailFormActivity)
            btnAcceptSeller.setOnClickListener(this@DetailFormActivity)
            btnRejectSeller.setOnClickListener(this@DetailFormActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnCallSeller.id -> {
                val rawLink = "https://wa.me/${form.phoneNumber}"
                val link = Uri.parse(rawLink)
                startActivity(Intent(Intent.ACTION_VIEW).setData(link))
            }
            binding.btnAcceptSeller.id -> {
                form.applicationStatus = AdminRepositoryImpl.APPROVED
                subscribe()
            }
            binding.btnRejectSeller.id -> {
                form.applicationStatus = AdminRepositoryImpl.REJECTED
                subscribe()
            }
        }
    }

    private fun subscribe() {
        viewModel.updateForm(form).observe(this) { response ->
            when (response) {
                is NetworkResponse.GenericException -> {
                    showLoading(false)
                    response.cause?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                }
                is NetworkResponse.Loading -> {
                    response.status?.let { binding.tvLoading.text = it }
                    showLoading(true)
                }
                is NetworkResponse.Success -> {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) binding.apply {
            tvLoading.visibility = View.VISIBLE
            progressLinear.visibility = View.VISIBLE
            btnAcceptSeller.visibility = View.INVISIBLE
            btnCallSeller.visibility = View.INVISIBLE
            btnRejectSeller.visibility = View.INVISIBLE
        } else binding.apply {
            tvLoading.visibility = View.GONE
            tvLoading.visibility = View.GONE
            btnAcceptSeller.visibility = View.VISIBLE
            btnCallSeller.visibility = View.VISIBLE
            btnRejectSeller.visibility = View.VISIBLE
        }
    }
}