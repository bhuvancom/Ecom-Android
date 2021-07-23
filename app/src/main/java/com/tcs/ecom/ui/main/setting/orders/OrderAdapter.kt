package com.tcs.ecom.ui.main.setting.orders

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tcs.ecom.databinding.ItemOrdersBinding
import com.tcs.ecom.models.SingleOrderResponse

/**
@author Bhuvaneshvar
Date    7/23/2021
Time    10:48 PM
Project Ecom
 */
class OrderAdapter(private val onClick: (SingleOrderResponse) -> Unit) :
    PagingDataAdapter<SingleOrderResponse, OrderAdapter.OrderViewHolder>(orderList) {

    inner class OrderViewHolder(itemOrdersBinding: ItemOrdersBinding) :
        RecyclerView.ViewHolder(itemOrdersBinding.root) {

        fun bind(singleOrderResponse: SingleOrderResponse) {
            Log.d(TAG, "bind: ")
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onClick(singleOrderResponse)
            }
        }
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(
                it
            )
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