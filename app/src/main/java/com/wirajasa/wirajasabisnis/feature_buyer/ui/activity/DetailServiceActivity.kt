package com.wirajasa.wirajasabisnis.feature_buyer.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.usecases.CurrencyFormatter
import com.wirajasa.wirajasabisnis.databinding.ActivityDetailServiceBinding
import com.wirajasa.wirajasabisnis.feature_auth.ui.viewmodel.LoginViewModel
import com.wirajasa.wirajasabisnis.feature_seller.ui.activity.UpdateServiceActivity
import com.wirajasa.wirajasabisnis.core.utility.constant.Constant.TEL
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump.EXTRA_SERVICE_POST
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump.RAW_LINK
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailServiceActivity : AppCompatActivity(), View.OnClickListener {
    private val binding: ActivityDetailServiceBinding by lazy {
        ActivityDetailServiceBinding.inflate(layoutInflater)
    }
    private var isSeller: Boolean = false
    private val viewModel by viewModels<LoginViewModel>()
    private var post: ServicePost? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_SERVICE_POST, ServicePost::class.java)
        } else {
            intent.getParcelableExtra<ServicePost>(EXTRA_SERVICE_POST) as ServicePost
        }

        val localProfile: UserProfile = viewModel.getProfile()
        Log.d(DetailServiceActivity::class.java.simpleName, localProfile.isSeller.toString())
        if (localProfile.isSeller && localProfile.uid == post?.uid) {
            binding.btnContactEdit.apply {
                background.setTint(getColor(R.color.dark_blue))
                text = getString(R.string.edit_service)
                setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    getDrawable(R.drawable.ic_edit),
                    null
                )

            }
            isSeller = true
        }

        binding.apply {
            tvPhoneNumber.text = getString(R.string.detail_service_phone_number, post?.phoneNumber)
            tvAddress.text = getString(R.string.detail_address, post?.address, post?.province)
            tvPrice.text = CurrencyFormatter().invoke(post?.price.toString())
            tvServiceName.text = post?.name
            tvUnit.text = getString(R.string.detail_unit, post?.unit)
            Glide.with(this@DetailServiceActivity)
                .load(post?.photoUrl)
                .into(ivDetail)

            btnContactEdit.setOnClickListener(this@DetailServiceActivity)
            btnContactWhatsapp.setOnClickListener(this@DetailServiceActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnContactWhatsapp.id -> {
                val rawLink = RAW_LINK + post?.phoneNumber
                val link = Uri.parse(rawLink)
                startActivity(Intent(Intent.ACTION_VIEW).setData(link))
            }
            binding.btnContactEdit.id -> {
                if (isSeller) {
                    val intent =
                        Intent(this@DetailServiceActivity, UpdateServiceActivity::class.java)
                    intent.putExtra(EXTRA_SERVICE_POST, post)
                    startActivity(intent)
                } else {
                    val dialPhoneIntent =
                        Intent(Intent.ACTION_DIAL, Uri.parse(TEL + post?.phoneNumber))
                    startActivity(dialPhoneIntent)
                }
            }
        }
    }
}