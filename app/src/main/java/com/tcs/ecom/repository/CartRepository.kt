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
    suspend fun makePayment(productForm: ProductForm) = cartApi.makePayment(productForm)
}
