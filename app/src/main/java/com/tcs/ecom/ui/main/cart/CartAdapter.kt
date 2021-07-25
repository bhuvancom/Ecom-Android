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
package com.tcs.ecom.ui.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tcs.ecom.databinding.ItemCartBinding
import com.tcs.ecom.models.OrderForm
import com.tcs.ecom.utility.Constants

/**
@author Bhuvaneshvar
Date    7/19/2021
Time    9:31 PM
Project Ecom
 */
class CartAdapter : ListAdapter<OrderForm, CartAdapter.CartViewHolder>(diffUtil) {

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: OrderForm) {
            binding.tvName.text = product.product.name
            binding.tvPrice.text = "${Constants.RUPPEE} ${product.product.price}"
            binding.tvQuantity.text = "Qty ${product.quantity}"
            binding.tvTotal.text = "Total ${Constants.RUPPEE} ${product.totalPrice}"
            Picasso.get().load(product.product.imgUrl).into(binding.ivProductImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<OrderForm>() {
            override fun areItemsTheSame(oldItem: OrderForm, newItem: OrderForm): Boolean =
                oldItem.product.id == newItem.product.id

            override fun areContentsTheSame(oldItem: OrderForm, newItem: OrderForm): Boolean =
                newItem == oldItem
        }

        private const val TAG = "CartAdapter"
    }
}
