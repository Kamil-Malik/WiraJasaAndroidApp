package com.wirajasa.wirajasabisnis.role_admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.utility.constant.ApplicationStatus
import com.wirajasa.wirajasabisnis.databinding.ItemRowApplicationFormBinding

class FormAdapter(
    private val onItemClicked: (SellerApplication) -> Unit
) : ListAdapter<SellerApplication, FormAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemRowApplicationFormBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {

        fun bind(item: SellerApplication) {
            when (item.applicationStatus) {
                ApplicationStatus.PENDING -> binding.root.background.setTint(
                    binding.root.context.getColor(
                        R.color.pending
                    )
                )

                ApplicationStatus.APPROVED -> binding.root.background.setTint(
                    binding.root.context.getColor(
                        R.color.approved
                    )
                )

                ApplicationStatus.REJECTED -> binding.root.background.setTint(
                    binding.root.context.getColor(
                        R.color.rejected
                    )
                )
            }
            binding.tvApplicationId.text = item.uid
            binding.tvApplicatoinCreator.text = item.fullName
            binding.root.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormAdapter.ViewHolder {
        return ViewHolder(
            binding = ItemRowApplicationFormBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FormAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SellerApplication>() {
            override fun areItemsTheSame(
                oldItem: SellerApplication, newItem: SellerApplication
            ): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(
                oldItem: SellerApplication, newItem: SellerApplication
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}