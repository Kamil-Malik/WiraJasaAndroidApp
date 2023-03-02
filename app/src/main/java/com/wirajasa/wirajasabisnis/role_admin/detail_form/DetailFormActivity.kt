package com.wirajasa.wirajasabisnis.role_admin.detail_form

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.core.utility.constant.ApplicationStatus.APPROVED
import com.wirajasa.wirajasabisnis.core.utility.constant.ApplicationStatus.REJECTED
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump.FORM
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump.RAW_LINK
import com.wirajasa.wirajasabisnis.databinding.ActivityDetailFormBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFormActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<DetailFormViewModel>()
    private lateinit var form: SellerApplication
    private val binding: ActivityDetailFormBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar).apply {
            onSupportNavigateUp()
        }
        form = if (VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(FORM, SellerApplication::class.java)
        } else {
            intent.getParcelableExtra(FORM)
        } as SellerApplication
        binding.apply {
            Glide.with(this@DetailFormActivity)
                .load(form.photoID)
                .into(ivIdCard)

            edtUid.setText(form.applicationId)
            edtFullName.setText(form.fullName)
            edtPhoneNumber.setText(getString(R.string.profile_phone_number, form.phoneNumber))
            edtProvince.setText(form.province)
            edtFullAddress.setText(form.address)
            edtServiceDescription.setText(form.serviceDescription)
            btnCallSeller.setOnClickListener(this@DetailFormActivity)
            btnAcceptSeller.setOnClickListener(this@DetailFormActivity)
            btnRejectSeller.setOnClickListener(this@DetailFormActivity)
        }
        subscribe()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnCallSeller.id -> {
                val rawLink = RAW_LINK + form.phoneNumber
                val link = Uri.parse(rawLink)
                startActivity(Intent(Intent.ACTION_VIEW).setData(link))
            }

            binding.btnAcceptSeller.id -> {
                form.applicationStatus = APPROVED
                viewModel.updateForm(form)
            }

            binding.btnRejectSeller.id -> {
                form.applicationStatus = REJECTED
                viewModel.updateForm(form)
            }
        }
    }

    private fun subscribe() {
        viewModel.formStatus.observe(this) { response ->
            when (response) {
                is NetworkResponse.GenericException -> {

                }

                is NetworkResponse.Loading -> {

                }

                is NetworkResponse.Success -> {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }
}