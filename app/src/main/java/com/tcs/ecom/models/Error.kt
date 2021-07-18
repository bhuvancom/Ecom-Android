package com.tcs.ecom.models

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("message")
    val reason: String,
    @SerializedName("httpCode")
    val code: Int
)
