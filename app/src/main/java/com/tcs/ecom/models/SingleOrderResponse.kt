/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
