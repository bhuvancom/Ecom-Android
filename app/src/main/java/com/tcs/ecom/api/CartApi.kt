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