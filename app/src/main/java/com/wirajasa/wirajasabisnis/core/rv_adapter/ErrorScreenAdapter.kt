package com.wirajasa.wirajasabisnis.core.rv_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wirajasa.wirajasabisnis.databinding.ErrorScreenBinding

class ErrorScreenAdapter(
    private val errorMessage: String?,
    private val onRetry: () -> Unit
) : RecyclerView.Adapter<ErrorScreenAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ErrorScreenAdapter.ViewHolder {
        return ViewHolder(
            binding = ErrorScreenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ErrorScreenAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1

    inner class ViewHolder(private val binding: ErrorScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvError.text = errorMessage
            binding.btnRetryTask.setOnClickListener {
                onRetry.invoke()
            }
        }
    }
}