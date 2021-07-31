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

import android.os.Bundle
import android.util.Log
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
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.tcs.ecom.BuildConfig
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentCartBinding
import com.tcs.ecom.models.Payment
import com.tcs.ecom.models.ProductForm
import com.tcs.ecom.utility.ApiResultState
import com.tcs.ecom.utility.Constants
import com.tcs.ecom.utility.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
    private lateinit var pymentSheet: PaymentSheet
    private var isPaymentSheenShown = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
        setupRecyclerView()
    }

    lateinit var paymentKey: String

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

        PaymentConfiguration.init(
            requireContext(),
            BuildConfig.STRIPE_KEY
        )
        pymentSheet = PaymentSheet(this@CartFragment) {
            onPaymentSheetResult(it)
        }

        binding.btnCheckout.setOnClickListener {
            Constants.CUURENT_CART.value?.let {
                val productForm = ProductForm(
                    it.cartItems,
                    it.users
                )
                cartViewModel.makePayment(Constants.CURRENT_USER.value!!)
                lifecycleScope.launchWhenCreated {
                    cartViewModel.paymentResponse.collectLatest { paymentState ->
                        when (paymentState) {
                            is ApiResultState.LOADING -> {
                                Log.d(TAG, "setupRecyclerView loading")
                                isPaymentSheenShown = false
                            }
                            is ApiResultState.ERROR -> {
                                Log.d(TAG, "setupRecyclerView: error ${paymentState.apiError}")
                                isPaymentSheenShown = false
                                Util.showAlert(
                                    requireContext(),
                                    {
                                        cartViewModel.makePayment(Constants.CURRENT_USER.value!!)
                                    },
                                    {},
                                    "Payment Failed",
                                    "The last attempt to payment got failed ${paymentState.apiError.reason}"
                                )
                            }
                            is ApiResultState.SUCCESS -> {
                                Log.d(TAG, "setupRecyclerView: success ${paymentState.result}")
                                if (!isPaymentSheenShown) presentPaymentSheet(paymentState.result)
                            }
                        }
                    }
                }
            }
        }


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
                        binding.tvMiddle.text = getString(R.string.no_item_in_cart)
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

    private fun presentPaymentSheet(result: Payment) {
        paymentKey = result.ephemeralKey
        isPaymentSheenShown = true
        Log.d(TAG, "presentPaymentSheet: ")
        pymentSheet.presentWithPaymentIntent(
            result.paymentIntent,
            PaymentSheet.Configuration(
                merchantDisplayName = "Bhuvancom",
                customer = PaymentSheet.CustomerConfiguration(
                    id = result.customer,
                    ephemeralKeySecret = result.ephemeralKey
                )
            )
        )
    }

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            PaymentSheetResult.Completed -> {
                Util.showToast(requireContext(), "Success")
                Log.d(TAG, "onPaymentSheetResult: $paymentResult")
                isPaymentSheenShown = true
                val orderForm =
                    ProductForm(users = Constants.CURRENT_USER.value!!, paymentId = paymentKey)
                cartViewModel.doOrder(orderForm)
            }
            PaymentSheetResult.Canceled -> {
                Log.d(TAG, "onPaymentSheetResult: $paymentResult")
                isPaymentSheenShown = false
            }
            is PaymentSheetResult.Failed -> {
                Log.d(TAG, "onPaymentSheetResult: failed ${paymentResult.error}")
                isPaymentSheenShown = false
            }
        }
    }

    companion object {
        private const val TAG = "CartFragment"
    }
}
