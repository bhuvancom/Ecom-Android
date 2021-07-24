package com.tcs.ecom.ui.main.setting.orders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentOrderDetailsBinding
import com.tcs.ecom.databinding.ItemOrderDetailsBinding
import com.tcs.ecom.models.OrderForm
import com.tcs.ecom.utility.Constants

/**
@author Bhuvaneshvar
Date    7/24/2021
Time    2:47 PM
Project Ecom
 */
class ShowOrderDetailsFragment : Fragment(R.layout.fragment_order_details) {
    private val args by navArgs<ShowOrderDetailsFragmentArgs>()
    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "ShowOrderDetailsFragment"
    }

    init {
        Log.d(TAG, "called: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderDetailsBinding.bind(view)
        val adapterProducts = OrderDetailAdapter(args.singleOrderItem.orderProducts)
        binding.apply {
            orderDetailRecycler.apply {
                adapter = adapterProducts
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
            tvTotal.text =
                "${args.singleOrderItem.numberOfProducts} items, total ${Constants.RUPPEE} ${args.singleOrderItem.totalOrderPrice}"
        }
    }

    class OrderDetailAdapter(private val items: List<OrderForm>) :
        RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {
        class OrderDetailViewHolder(private val binding: ItemOrderDetailsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(itm: OrderForm) {
                binding.apply {
                    tvName.text = itm.product.name
                    Picasso.get().load(itm.product.imgUrl)
                        .into(ivProductImage)
                    tvPrice.text =
                        "${itm.quantity} x ${itm.product.price} = ${Constants.RUPPEE} ${itm.totalPrice}"
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder =
            OrderDetailViewHolder(
                ItemOrderDetailsBinding.inflate(LayoutInflater.from(parent.context))
            )

        override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
            val itm = items[position]
            holder.bind(itm)
        }

        override fun getItemCount(): Int = items.size
    }
}