package com.tcs.ecom.models

import com.google.gson.annotations.SerializedName

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    8:28 PM
Project Ecom
 */
data class ProductResponse(
    @SerializedName("content")
    val products: List<Product> = listOf(),
    @SerializedName("last")
    val isLast: Boolean,
    @SerializedName("totalPages")
    val totalPage: Int,
    @SerializedName("number")
    val currentPage: Int
)