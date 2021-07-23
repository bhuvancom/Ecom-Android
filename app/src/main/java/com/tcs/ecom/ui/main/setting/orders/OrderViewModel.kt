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