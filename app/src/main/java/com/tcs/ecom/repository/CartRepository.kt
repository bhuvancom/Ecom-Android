package com.tcs.ecom.repository

import com.tcs.ecom.api.CartApi
import com.tcs.ecom.models.ProductForm
import javax.inject.Inject

/**
@author Bhuvaneshvar
Date    7/19/2021
Time    7:47 PM
Project Ecom
 */

class CartRepository @Inject constructor(private val cartApi: CartApi) {
    suspend fun upsertCart(productForm: ProductForm) = cartApi.saveCart(productForm)
    suspend fun getCart(userId: Long) = cartApi.getThisUserCart(userId)
}
