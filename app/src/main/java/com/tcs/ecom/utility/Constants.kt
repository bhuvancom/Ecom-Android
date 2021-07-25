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
