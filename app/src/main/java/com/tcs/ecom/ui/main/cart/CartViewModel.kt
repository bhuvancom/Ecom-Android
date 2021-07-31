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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcs.ecom.models.ApiError
import com.tcs.ecom.models.CartResponse
import com.tcs.ecom.models.OrderForm
import com.tcs.ecom.models.Payment
import com.tcs.ecom.models.Product
import com.tcs.ecom.models.ProductForm
import com.tcs.ecom.models.SingleOrderResponse
import com.tcs.ecom.models.Users
import com.tcs.ecom.repository.CartRepository
import com.tcs.ecom.utility.ApiResultState
import com.tcs.ecom.utility.Constants
import com.tcs.ecom.utility.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    10:22 PM
Project Ecom
 */
@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {
    companion object {
        private const val TAG = "CartViewModel"
    }

    private val _cart = MutableLiveData<ApiResultState<CartResponse>>()
    val cart: LiveData<ApiResultState<CartResponse>> get() = _cart
    private val _paymentResponse = MutableStateFlow<ApiResultState<Payment>>(ApiResultState.START)
    val paymentResponse = _paymentResponse.asStateFlow()

    init {
        loadInitialCart()
    }

    fun retry() {
        loadInitialCart()
    }

    fun makePayment(user: Users) {
        viewModelScope.launch {
            _paymentResponse.emit(
                Util.doSafeCall {
                    cartRepository.makePayment(user)
                }
            )
        }
    }

    private fun loadInitialCart() {
        getUserCart(Constants.CURRENT_USER.value!!.id!!)
    }

    private fun getUserCart(userId: Long) {
        viewModelScope.launch {
            _cart.postValue(ApiResultState.START)
            try {
                _cart.postValue(ApiResultState.LOADING)
                val cart = cartRepository.getCart(userId)
                if (cart.isSuccessful && cart.body() != null) {
                    Constants.CUURENT_CART.postValue(cart.body()!!)
                    _cart.postValue(ApiResultState.SUCCESS(cart.body()!!))
                } else {
                    cart.errorBody()?.apply {
                        val error = this.string()
                        val apiError = Util.getApiError(error)
                        _cart.postValue(ApiResultState.ERROR(apiError))
                    }
                }
            } catch (error: Exception) {
                _cart.postValue(ApiResultState.ERROR(ApiError("${error.message}", 500)))
            }
        }
    }

    /**
     * This will save one cart of the user.
     */
    private fun upserCart(productForm: ProductForm) {
        viewModelScope.launch {
            _cart.postValue(ApiResultState.START)
            try {
                _cart.postValue(ApiResultState.LOADING)
                val upsertCart = cartRepository.upsertCart(productForm)
                if (upsertCart.isSuccessful && upsertCart.body() != null) {
                    _cart.postValue(ApiResultState.SUCCESS(upsertCart.body()!!))
                    Constants.CUURENT_CART.postValue(upsertCart.body())
                } else {
                    upsertCart.errorBody()?.apply {
                        val error = this.string()
                        val apiError = Util.getApiError(error)
                        _cart.postValue(ApiResultState.ERROR(apiError))
                    }
                }
            } catch (error: Exception) {
                _cart.postValue(ApiResultState.ERROR(ApiError("${error.message}", 500)))
            }
        }
    }

    fun addToCart(product: Product) {

        if (Constants.CUURENT_CART.value == null) {
            val cartResponse = CartResponse(users = Constants.CURRENT_USER.value!!)
            Constants.CUURENT_CART.postValue(cartResponse)
        }

        Constants.CUURENT_CART.value?.let {
            val orderForm = OrderForm(product)
            val addItemToCart = Util.addItemToCart(orderForm, it)
            val productForm = ProductForm(
                addItemToCart.cartItems,
                addItemToCart.users
            )
            upserCart(productForm)
        }
    }

    fun removeProductFromCart(orderForm: OrderForm) {

        Constants.CUURENT_CART.value?.let {
            val removeProductFromCart = Util.removeProductFromCart(orderForm, it)
            val productForm = ProductForm(
                removeProductFromCart.cartItems,
                removeProductFromCart.users
            )
            upserCart(productForm)
        }
    }
    private val _doOrderResponse = MutableLiveData<ApiResultState<SingleOrderResponse>>()
    val doOrderResponse: LiveData<ApiResultState<SingleOrderResponse>> = _doOrderResponse


    fun doOrder(orderForm: ProductForm) {
        viewModelScope.launch {
            _doOrderResponse.value = Util.doSafeCall {
                cartRepository.doOrder(orderForm)
            }
        }
    }
}
