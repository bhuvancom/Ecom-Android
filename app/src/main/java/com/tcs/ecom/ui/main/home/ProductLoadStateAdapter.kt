package com.tcs.ecom.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tcs.ecom.databinding.ProductLoadLayoutBinding

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    9:03 PM
Project Ecom
 */
class ProductLoadStateAdapter(private inline val retry: () -> Unit) :
    LoadStateAdapter<ProductLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(private val binding: ProductLoadLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState !is LoadState.Loading
                tvError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ProductLoadLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding)
    }
}
