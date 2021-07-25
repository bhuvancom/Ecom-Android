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
