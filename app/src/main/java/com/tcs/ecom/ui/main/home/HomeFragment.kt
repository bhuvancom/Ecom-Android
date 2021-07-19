package com.tcs.ecom.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentHomeBinding
import com.tcs.ecom.ui.main.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    2:54 PM
Project Ecom
 */
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    companion object {
        private const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val productViewModel by viewModels<ProductViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        setupRecyclerView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setupRecyclerView() {
        val adapterPaging = ProductAdapter(onAddToCartClick = {
            cartViewModel.addToCart(it)
        }, onProductClick = {
            Log.d(TAG, "setupRecyclerView: product clicked  $it")
        })

        binding.homeRecyclerView.apply {
            adapter = adapterPaging.withLoadStateHeaderAndFooter(
                header = ProductLoadStateAdapter { adapterPaging.retry() },
                footer = ProductLoadStateAdapter { adapterPaging.retry() }
            )
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                productViewModel.products.observe(viewLifecycleOwner) { pagingData ->
                    adapterPaging.submitData(viewLifecycleOwner.lifecycle, pagingData)
                }
            }
        }

        adapterPaging.addLoadStateListener { combinedLoadStates ->
            binding.apply {
                progressBar.isVisible = combinedLoadStates.source.refresh is LoadState.Loading

                homeRecyclerView.isVisible =
                    combinedLoadStates.source.refresh is LoadState.NotLoading

                btnRetry.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                tvError.isVisible = combinedLoadStates.source.refresh is LoadState.Error

                if (combinedLoadStates.source.refresh is LoadState.NotLoading &&
                    combinedLoadStates.append.endOfPaginationReached &&
                    adapterPaging.itemCount < 1
                ) {
                    homeRecyclerView.isVisible = false
                    tvNoResult.isVisible = true
                } else {
                    tvNoResult.isVisible = false
                }
            }

        }
    }
}