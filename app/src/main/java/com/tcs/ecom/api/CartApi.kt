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
package com.tcs.ecom.api

import com.tcs.ecom.models.CartResponse
import com.tcs.ecom.models.ProductForm
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
@author Bhuvaneshvar
Date    7/19/2021
Time    7:33 PM
Project Ecom
 */
interface CartApi {
    @GET("cart/user/{user_id}")
    suspend fun getThisUserCart(@Path("user_id") userId: Long): Response<CartResponse>

    @POST("cart")
    suspend fun saveCart(@Body productForm: ProductForm): Response<CartResponse>
}
