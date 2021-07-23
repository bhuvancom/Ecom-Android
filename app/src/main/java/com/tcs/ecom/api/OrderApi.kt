package com.tcs.ecom.api

import com.tcs.ecom.models.OrderForm
import com.tcs.ecom.models.OrderResponse
import com.tcs.ecom.models.SingleOrderResponse
import retrofit2.Response
import retrofit2.http.*

/**
@author Bhuvaneshvar
Date    7/23/2021
Time    9:09 PM
Project Ecom
 */
interface OrderApi {
    @POST("orders")
    suspend fun doOrder(@Body orderForm: OrderForm): Response<SingleOrderResponse>

    @GET("users/orders/{user_id}")
    suspend fun getOrderOfThisUser(
        @Path("user_id") id: Long,
        @Query("page") page: Int = 1
    ): Response<OrderResponse>
}