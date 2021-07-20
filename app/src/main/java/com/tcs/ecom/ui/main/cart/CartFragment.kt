package com.tcs.ecom.ui.main.cart

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentCartBinding
import com.tcs.ecom.utility.ApiResultState
import com.tcs.ecom.utility.Constants
import com.tcs.ecom.utility.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    7:37 PM
Project Ecom
 */
@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel by viewModels<CartViewModel>()
    private val cartAdapter by lazy {
        CartAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
        setupRecyclerView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setupRecyclerView() {
        binding.rvCart.apply {
            adapter = cartAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        val swipe: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    cartAdapter.currentList.getOrNull(pos)?.let {
                        cartAdapter.submitList(null)
                        cartViewModel.removeProductFromCart(it)
                    }
                }
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(binding.rvCart)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                cartViewModel.retry()

                cartViewModel.cart.observe(viewLifecycleOwner) {
                    binding.btnCheckout.isEnabled =
                        (it !is ApiResultState.LOADING || it is ApiResultState.ERROR) &&
                                Constants.CUURENT_CART.value?.cartItems?.isNotEmpty() ?: false

                    binding.progressBar2.isVisible = it is ApiResultState.LOADING
                    binding.tvMiddle.isVisible = it !is ApiResultState.LOADING

                    if (it is ApiResultState.ERROR) {
                        binding.rvCart.isVisible = false
                        Util.showAlert(
                            requireContext(),
                            onYes = {
                                cartViewModel.retry()
                            },
                            onNo = {},
                            "Cart Loading Failed",
                            "Error occurred ${it.apiError.reason}\nRetry?"
                        )
                        binding.tvMiddle.isVisible = true
                        binding.tvMiddle.text = getString(R.string.error_loading)
                    }

                    if (it is ApiResultState.SUCCESS) {
                        cartAdapter.submitList(it.result.cartItems)
                        binding.tvMiddle.isVisible = it.result.cartItems.isNullOrEmpty()
                        binding.rvCart.isVisible = it.result.cartItems.isNotEmpty()
                        binding.btnCheckout.text =
                            "Checkout ${Constants.RUPPEE} ${it.result.totalCartPrice}"
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "CartFragment"
    }
}