package com.tcs.ecom.ui.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcs.ecom.models.*
import com.tcs.ecom.repository.CartRepository
import com.tcs.ecom.utility.ApiResultState
import com.tcs.ecom.utility.Constants
import com.tcs.ecom.utility.Util
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val cart: LiveData<ApiResultState<CartResponse>> = _cart

    fun getUserCart(userId: Long) {
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
    fun upserCart(productForm: ProductForm) {
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
        Constants.CUURENT_CART.value?.let {
            
        }
    }

    fun removeProductFromCart(orderForm: OrderForm) {
        //todo this is working fine but server is not able to remove from db check
        Constants.CUURENT_CART.value?.let {
            val removeProductFromCart = Util.removeProductFromCart(orderForm, it)
            val productForm = ProductForm(
                removeProductFromCart.cartItems,
                removeProductFromCart.users
            )
            upserCart(productForm)
        }
    }
}