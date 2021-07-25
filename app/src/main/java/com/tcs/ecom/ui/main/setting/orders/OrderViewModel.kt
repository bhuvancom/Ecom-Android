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
package com.tcs.ecom.ui.main.setting.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tcs.ecom.models.OrderForm
import com.tcs.ecom.models.SingleOrderResponse
import com.tcs.ecom.repository.OrderRepository
import com.tcs.ecom.utility.ApiResultState
import com.tcs.ecom.utility.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
@author Bhuvaneshvar
Date    7/23/2021
Time    9:38 PM
Project Ecom
 */
@HiltViewModel
class OrderViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {
    private val _doOrderResponse = MutableLiveData<ApiResultState<SingleOrderResponse>>()

    val doOrderResponse: LiveData<ApiResultState<SingleOrderResponse>> = _doOrderResponse

    lateinit var result: LiveData<PagingData<SingleOrderResponse>>

    fun doOrder(orderForm: OrderForm) {
        viewModelScope.launch {
            _doOrderResponse.value = Util.doSafeCall {
                orderRepository.doOrder(orderForm)
            }
        }
    }

    fun getThisUserOrders(usrId: Long) {
        result = orderRepository.getThisUserOrder(usrId).cachedIn(viewModelScope)
    }
}
