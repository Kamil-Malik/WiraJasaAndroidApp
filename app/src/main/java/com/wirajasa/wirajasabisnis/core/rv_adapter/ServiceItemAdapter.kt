package com.wirajasa.wirajasabisnis.core.rv_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.usecases.CurrencyFormatter
import com.wirajasa.wirajasabisnis.databinding.ItemListDashboardBinding

class ServiceItemAdapter(
    val onItemSelected: (ServicePost) -> Unit
) : ListAdapter<ServicePost, ServiceItemAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceItemAdapter.ViewHolder {
        return ViewHolder(
            binding = ItemListDashboardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ServiceItemAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemListDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ServicePost) {
            Glide.with(binding.root.context)
                .load(item.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.ivItemPhoto)

            binding.tvTitle.text = item.name
            binding.tvProvince.text = item.province
            binding.tvPrice.text = CurrencyFormatter().invoke(item.price.toString())
            binding.root.setOnClickListener {
                onItemSelected.invoke(item)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ServicePost>() {
            override fun areItemsTheSame(oldItem: ServicePost, newItem: ServicePost): Boolean {
                return oldItem.serviceId == newItem.serviceId
            }

            override fun areContentsTheSame(
                oldItem: ServicePost,
                newItem: ServicePost
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}