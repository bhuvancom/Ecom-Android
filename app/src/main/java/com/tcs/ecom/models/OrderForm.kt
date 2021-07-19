package com.tcs.ecom.models

import com.google.gson.annotations.SerializedName

data class OrderForm(
    @SerializedName("product")
    val product: Product,

    @SerializedName("quantity")
    var quantity: Int = 1,

    @SerializedName("totalPrice") //todo see if this is giving error
    val totalPrice: Double = 0.0
)
