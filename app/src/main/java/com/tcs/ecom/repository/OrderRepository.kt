package com.tcs.ecom.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.tcs.ecom.api.OrderApi
import com.tcs.ecom.models.OrderForm
import com.tcs.ecom.ui.main.setting.orders.OrderPaging
import javax.inject.Inject

/**
@author Bhuvaneshvar
Date    7/23/2021
Time    9:22 PM
Project Ecom
 */
class OrderRepository @Inject constructor(private val orderApi: OrderApi) {

    suspend fun doOrder(orderForm: OrderForm) = orderApi.doOrder(orderForm)
    fun getThisUserOrder(userId: Long) = Pager(
        config = PagingConfig(
            pageSize = 5,
            maxSize = 2000,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            OrderPaging(orderApi, userId)
        }
    ).liveData
}
