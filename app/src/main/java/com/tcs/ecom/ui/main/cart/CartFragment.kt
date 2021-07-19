package com.tcs.ecom.ui.main.cart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

    private fun setupRecyclerView() {
        binding.rvCart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
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
                val pos = viewHolder.bindingAdapterPosition
                cartAdapter.currentList.getOrNull(pos)?.let {
                    cartViewModel.removeProductFromCart(it)
                }
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(binding.rvCart)

        cartViewModel.getUserCart(Constants.CURRENT_USER!!.id!!)

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                Log.d(TAG, "setupRecyclerView: collect start")
                cartViewModel.cart.observe(viewLifecycleOwner) {
                    Log.d(TAG, "setupRecyclerView: $it")

                    binding.rvCart.isVisible = it !is ApiResultState.LOADING
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
                                cartViewModel.getUserCart(Constants.CURRENT_USER!!.id!!)
                            },
                            onNo = {},
                            "Cart Loading Failed",
                            "Error occurred ${it.apiError.reason}\nRetry?"
                        )
                        binding.tvMiddle.text = getString(R.string.error_loading)
                    }

                    if (it is ApiResultState.SUCCESS) {
                        Toast.makeText(
                            requireContext(),
                            "Swipe left or right to remove product",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.tvMiddle.isVisible = it.result.cartItems.isNullOrEmpty()
                        Log.d(TAG, "setupRecyclerView: success")
                        binding.rvCart.isVisible = it.result.cartItems.isNotEmpty()
                        cartAdapter.submitList(it.result.cartItems)

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