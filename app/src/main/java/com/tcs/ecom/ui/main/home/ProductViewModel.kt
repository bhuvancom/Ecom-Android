package com.tcs.ecom.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tcs.ecom.models.Product
import com.tcs.ecom.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    9:08 PM
Project Ecom
 */
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) :
    ViewModel() {
    val currentSearch = MutableLiveData("")
    private lateinit var result: LiveData<PagingData<Product>>
    val products
        get() = result

    init {
        search()
    }

    private fun search() {
        result = currentSearch.switchMap {
            productRepository.getProducts(it).cachedIn(viewModelScope)
        }
    }

    fun setSearch(query: String) {
        currentSearch.value = query
    }
}
