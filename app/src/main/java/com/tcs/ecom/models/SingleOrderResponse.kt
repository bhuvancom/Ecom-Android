package com.tcs.ecom.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
@author Bhuvaneshvar
Date    7/23/2021
Time    9:17 PM
Project Ecom
 */
@Parcelize
data class SingleOrderResponse(
    val id: Long? = null,
    @SerializedName("dateCreated")
    val dateCreated: String,
    val status: String,
    val orderProducts: List<OrderForm> = listOf(),
    val user: Users,
    val numberOfProducts: Long,
    val totalOrderPrice: Double
) : Parcelable
