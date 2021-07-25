package com.tcs.ecom.api

import com.tcs.ecom.models.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    7:53 PM
Project Ecom
 */
interface ProductApi {
    @GET("products")
    suspend fun getProduct(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 5
    ): Response<ProductResponse>
}
