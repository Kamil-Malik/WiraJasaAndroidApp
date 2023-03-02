package com.wirajasa.wirajasabisnis.core.rv_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wirajasa.wirajasabisnis.databinding.LoadingScreenBinding

class LoadingScreenAdapter(
    val title: String?
) : RecyclerView.Adapter<LoadingScreenAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LoadingScreenAdapter.ViewHolder {
        return ViewHolder(
           binding = LoadingScreenBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
        )
    }

    override fun onBindViewHolder(holder: LoadingScreenAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1

    inner class ViewHolder(private val binding: LoadingScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvScreenLoadingStatus.text = title
        }
    }
}