package com.tcs.ecom.models

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("userEmail")
    val email: String = "",
    @SerializedName("userPassword")
    var password: String = "",
    @SerializedName("address")
    var address: String = ""
)
