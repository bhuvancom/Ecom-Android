package com.tcs.ecom.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    8:18 PM
Project Ecom
 */
@Parcelize
data class Product(
    @SerializedName("id") var id: Long? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("pictureUrl")
    val imgUrl: String
) : Parcelable
