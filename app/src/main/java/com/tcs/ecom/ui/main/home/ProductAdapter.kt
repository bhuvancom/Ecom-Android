package com.tcs.ecom.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tcs.ecom.databinding.ItemProductBinding
import com.tcs.ecom.models.Product

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    8:52 PM
Project Ecom
 */
class ProductAdapter(
    private val onProductClick: (Long) -> Unit,
    private val onAddToCartClick: (Long) -> Unit
) : PagingDataAdapter<Product, ProductAdapter.ProductViewHolder>(productComparator) {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Picasso.get().load(product.imgUrl)
                    .into(ivProductImage)
                tvName.text = product.name
                tvPrice.text = "${product.price}"
                product.id?.let {
                    addToCart.setOnClickListener {
                        onAddToCartClick(product.id!!)
                    }
                }
            }
        }

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = getItem(position)
                    product?.let {
                        it.id?.let(onProductClick)
                    }
                }
            }
        }
    }

    companion object {
        private val productComparator = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }
}