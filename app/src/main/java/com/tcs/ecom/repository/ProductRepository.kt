package com.tcs.ecom.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.tcs.ecom.api.ProductApi
import com.tcs.ecom.ui.main.home.ProductPaging
import javax.inject.Inject

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    8:34 PM
Project Ecom
 */
class ProductRepository @Inject constructor(private val productApi: ProductApi) {
    fun getProducts(query: String) = Pager(
        config = PagingConfig(
            pageSize = 5,
            maxSize = 200,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            ProductPaging(productApi, query)
        }

    ).liveData
}