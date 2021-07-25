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
package com.tcs.ecom.utility

import androidx.lifecycle.MutableLiveData
import com.tcs.ecom.models.CartResponse
import com.tcs.ecom.models.Users

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    3:17 PM
Project Ecom
 */
object Constants {
    const val QUERY = "QUERY"
    var CURRENT_USER: MutableLiveData<Users> = MutableLiveData()
    const val USER_DETAIL = "USER"
    const val MY_SHARED_PREF = "ECOM"
    const val CHANNEL_ID = "Ecom General"
    const val CHANNEL_ID_WITHOUT_SOUND = "Ecom Low priority"
    const val CHANNEL_ID_STICKY = "Ecom High"
    const val RUPPEE = "₹"
    var CUURENT_CART = MutableLiveData<CartResponse>()
}
