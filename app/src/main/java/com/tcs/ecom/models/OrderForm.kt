package com.tcs.ecom.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderForm(
    @SerializedName("product")
    val product: Product,

    @SerializedName("quantity")
    var quantity: Int = 1,

    @SerializedName("totalPrice") //todo see if this is giving error
    val totalPrice: Double = 0.0
) : Parcelable
