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
            maxSize = 2000,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            ProductPaging(productApi, query)
        }

    ).liveData
}
