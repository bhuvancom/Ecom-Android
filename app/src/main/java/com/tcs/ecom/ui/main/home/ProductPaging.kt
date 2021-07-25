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
package com.tcs.ecom.ui.main.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tcs.ecom.api.ProductApi
import com.tcs.ecom.models.Product
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    8:17 PM
Project Ecom
 */
private const val PAGE_ONE = 1

class ProductPaging @Inject constructor(
    private val productRepository: ProductApi,
    private val searchQuery: String
) :
    PagingSource<Int, Product>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: PAGE_ONE
        return try {
            Log.d(TAG, "load: in load ${params.loadSize} key $params")
            // delay(2000L)
            val response = productRepository.getProduct(searchQuery, position, params.loadSize)
            if (response.isSuccessful && response.body() != null) {
                LoadResult.Page(
                    data = response.body()!!.products,
                    prevKey = if (position == PAGE_ONE) null else position - 1,
                    nextKey = if (response.body()!!.isLast) null else position + 1
                )
            } else {
                LoadResult.Error(RuntimeException("Error Getting products ${response.code()}"))
            }
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val TAG = "ProductPaging"
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }
}
