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

import com.google.gson.annotations.SerializedName

/**
@author Bhuvaneshvar
Date    7/19/2021
Time    7:40 PM
Project Ecom
 */
data class CartResponse(
    @SerializedName("cartItems")
    val cartItems: MutableList<OrderForm> = mutableListOf(),
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("numberOfProducts")
    val numberOfProducts: Long = 0,
    @SerializedName("totalCartPrice")
    val totalCartPrice: Double = 0.0,
    @SerializedName("user")
    val users: Users
)
