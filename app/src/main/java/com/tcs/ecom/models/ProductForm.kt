package com.tcs.ecom.models

import com.google.gson.annotations.SerializedName

/**
@author Bhuvaneshvar
Date    7/19/2021
Time    7:35 PM
Project Ecom
 */
data class ProductForm(
    @SerializedName("productOrders")
    val productOrders: MutableList<OrderForm> = mutableListOf(),
    @SerializedName("user")
    val users: Users
)
