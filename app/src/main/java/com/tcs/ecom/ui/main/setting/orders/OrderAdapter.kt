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
package com.tcs.ecom.ui.main.setting.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tcs.ecom.databinding.ItemOrdersBinding
import com.tcs.ecom.models.SingleOrderResponse
import com.tcs.ecom.utility.Constants

/**
@author Bhuvaneshvar
Date    7/23/2021
Time    10:48 PM
Project Ecom
 */
class OrderAdapter(private inline val onClick: (SingleOrderResponse) -> Unit) :
    PagingDataAdapter<SingleOrderResponse, OrderAdapter.OrderViewHolder>(orderList) {

    inner class OrderViewHolder(private val itemOrdersBinding: ItemOrdersBinding) :
        RecyclerView.ViewHolder(itemOrdersBinding.root) {

        fun bind(singleOrderResponse: SingleOrderResponse) {
            itemOrdersBinding.apply {
                Picasso.get().load(singleOrderResponse.orderProducts[0].product.imgUrl)
                    .into(ivProductImage)
                tvStatusDate.text =
                    "${singleOrderResponse.status} on ${singleOrderResponse.dateCreated}"
                tvNumberOfProduct.text =
                    "${singleOrderResponse.numberOfProducts} item(s) including ${singleOrderResponse.orderProducts[0].product.name}"
                tvTotal.text = "Total : ${Constants.RUPPEE} ${singleOrderResponse.totalOrderPrice}"
            }
        }
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(
                item
            )
            holder.itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder =
        OrderViewHolder(
            ItemOrdersBinding.inflate(LayoutInflater.from(parent.context))
        )

    companion object {
        val orderList = object : DiffUtil.ItemCallback<SingleOrderResponse>() {
            override fun areItemsTheSame(
                oldItem: SingleOrderResponse,
                newItem: SingleOrderResponse
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SingleOrderResponse,
                newItem: SingleOrderResponse
            ): Boolean = oldItem == newItem
        }

        private const val TAG = "OrderAdapter"
    }
}
