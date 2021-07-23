package com.tcs.ecom.models

import com.google.gson.annotations.SerializedName

/**
@author Bhuvaneshvar
Date    7/23/2021
Time    9:14 PM
Project Ecom
 */
data class OrderResponse(
    @SerializedName("content")
    val products: List<SingleOrderResponse> = listOf(),
    @SerializedName("last")
    val isLast: Boolean,
    @SerializedName("totalPages")
    val totalPage: Int,
    @SerializedName("number")
    val currentPage: Int
) {
}