package com.tcs.ecom.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("userEmail")
    val email: String = "",
    @SerializedName("userPassword")
    var password: String = "",
    @SerializedName("address")
    var address: String = ""
) : Parcelable
