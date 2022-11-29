package com.wirajasa.wirajasabisnis.ui.seller.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.data.model.ServicePost
import com.wirajasa.wirajasabisnis.databinding.ItemListDashboardBinding
import com.wirajasa.wirajasabisnis.presentation.service.DetailServiceActivity

class DashboardAdapter(private val context: Context, private val servicePostList: MutableList<ServicePost>)
    : RecyclerView.Adapter<DashboardAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListDashboardBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding,context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = servicePostList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,DetailServiceActivity::class.java)
            val service = ServicePost(
                uid = data.uid,
                serviceId = data.serviceId,
                name = data.name,
                price = data.price,
                unit = data.unit,
                address = data.address,
                province = data.province,
                phoneNumber = data.phoneNumber,
                photoUrl = data.photoUrl
            )
            intent.putExtra(DetailServiceActivity.EXTRA_SERVICE_POST, service)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = servicePostList.size

    class MyViewHolder(val binding: ItemListDashboardBinding, private val context: Context):
            RecyclerView.ViewHolder(binding.root){
                fun bind(data: ServicePost){
                    binding.tvPrice.text = context.getString(R.string.rupiah,data.price)
                    binding.tvTitle.text = data.name
                    Glide.with(context)
                        .load(data.photoUrl)
                        .into(binding.ivItemPhoto)
                }
            }
}