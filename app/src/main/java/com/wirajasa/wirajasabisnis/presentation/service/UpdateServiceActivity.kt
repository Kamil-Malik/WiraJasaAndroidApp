package com.wirajasa.wirajasabisnis.presentation.service

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.databinding.ActivityUpdateServiceBinding

class UpdateServiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUpdateServiceBinding
    private var post: ServicePost? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(DetailServiceActivity.EXTRA_SERVICE_POST, ServicePost::class.java)
        } else {
            intent.getParcelableExtra<ServicePost>(DetailServiceActivity.EXTRA_SERVICE_POST) as ServicePost
        }

        binding.edtAddress.setText(post?.address)
        binding.edtService.setText(post?.name)
        binding.edtPrice.setText(post?.price.toString())
        binding.edtPhone.setText(post?.phoneNumber)
        binding.edtUnit.setText(post?.unit)
        binding.edtProvince.setText(post?.province)
        if (post?.photoUrl != null){
            binding.tvIcon.visibility = View.GONE
            binding.ivIcon.visibility = View.GONE
            Glide.with(this@UpdateServiceActivity)
                .load(post?.photoUrl)
                .into(binding.ivService)
        }
    }
}