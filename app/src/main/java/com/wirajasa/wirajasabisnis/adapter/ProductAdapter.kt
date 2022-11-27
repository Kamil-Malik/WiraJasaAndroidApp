package com.wirajasa.wirajasabisnis.adapter

import android.content.Intent
import android.content.Intent.EXTRA_TITLE
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wirajasa.wirajasabisnis.data.model.Product
import com.wirajasa.wirajasabisnis.databinding.ItemListDashboardBinding
import com.wirajasa.wirajasabisnis.presentation.product.DetailFragment

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val listProduct = ArrayList<Product>()

    fun setAllData(data: List<Product>) {
        listProduct.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = ItemListDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int = listProduct.size

    inner class ProductViewHolder(private val view: ItemListDashboardBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(product: Product) {
            view.apply {
                tvTitle.text = product.title
                tvPrice.text = product.price

                Glide.with(itemView.context)
                    .load(product.photo)
                    .into(view.ivItemPhoto)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailFragment::class.java)
                    intent.putExtra(EXTRA_TITLE, product.title)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}