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

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentOrdersBinding
import com.tcs.ecom.models.SingleOrderResponse
import com.tcs.ecom.ui.main.home.ProductLoadStateAdapter
import com.tcs.ecom.utility.Constants
import dagger.hilt.android.AndroidEntryPoint

/**
@author Bhuvaneshvar
Date    7/23/2021
Time    10:43 PM
Project Ecom
 */
@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_orders) {

    companion object {
        private const val TAG = "OrderFragment"
    }

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private val orderAdapter by lazy {
        OrderAdapter {
            handleClickOnOrder(it)
        }
    }

    private val orderViewModel by viewModels<OrderViewModel>()

    private fun handleClickOnOrder(orderResponse: SingleOrderResponse) {
        val toShowOrderDetailsFragment =
            OrderFragmentDirections.actionOrderFragmentToShowOrderDetailsFragment(orderResponse)
        findNavController().navigate(toShowOrderDetailsFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrdersBinding.bind(view)
        Log.d(TAG, "onViewCreated: ")
        setupRecyclerView()
        lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                orderViewModel.getThisUserOrders(Constants.CURRENT_USER.value!!.id!!)
                orderViewModel.result.observe(viewLifecycleOwner) { pageData ->
                    orderAdapter.submitData(viewLifecycleOwner.lifecycle, pageData)
                }
            }
        }
    }

    private fun setupRecyclerView() {

        binding.orderRecyclerView.apply {
            adapter = orderAdapter.withLoadStateHeaderAndFooter(
                header = ProductLoadStateAdapter { orderAdapter.retry() },
                footer = ProductLoadStateAdapter { orderAdapter.retry() }
            )
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding.btnRetry.setOnClickListener {
            orderViewModel.getThisUserOrders(Constants.CURRENT_USER.value!!.id!!)
        }
        orderAdapter.addLoadStateListener { combinedLoadStates ->
            binding.apply {
                progressBar.isVisible = combinedLoadStates.source.refresh is LoadState.Loading

                orderRecyclerView.isVisible =
                    combinedLoadStates.source.refresh is LoadState.NotLoading

                btnRetry.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                tvError.isVisible = combinedLoadStates.source.refresh is LoadState.Error

                if (combinedLoadStates.source.refresh is LoadState.NotLoading &&
                    combinedLoadStates.append.endOfPaginationReached &&
                    orderAdapter.itemCount < 1
                ) {
                    orderRecyclerView.isVisible = false
                    tvNoResult.isVisible = true
                } else {
                    tvNoResult.isVisible = false
                }
            }
        }
    }
}
