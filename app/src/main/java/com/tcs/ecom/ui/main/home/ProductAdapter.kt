/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tcs.ecom.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tcs.ecom.databinding.ItemProductBinding
import com.tcs.ecom.models.Product
import com.tcs.ecom.utility.Constants

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    8:52 PM
Project Ecom
 */
class ProductAdapter(
    private inline val onProductClick: (Long) -> Unit,
    private inline val onAddToCartClick: (Product) -> Unit
) : PagingDataAdapter<Product, ProductAdapter.ProductViewHolder>(productComparator) {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Picasso.get().load(product.imgUrl)
                    .into(ivProductImage)
                tvName.text = product.name
                tvPrice.text = "${Constants.RUPPEE} ${product.price}"
                product.id?.let {
                    addToCart.setOnClickListener {
                        onAddToCartClick(product)
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
