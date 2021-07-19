package com.tcs.ecom.models

import com.google.gson.annotations.SerializedName

/**
@author Bhuvaneshvar
Date    7/19/2021
Time    7:40 PM
Project Ecom
 */
data class CartResponse(
    @SerializedName("cartItems")
    val cartItems: MutableList<OrderForm> = mutableListOf(),
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("numberOfProducts")
    val numberOfProducts: Long = 0,
    @SerializedName("totalCartPrice")
    val totalCartPrice: Double = 0.0,
    @SerializedName("user")
    val users: Users
)