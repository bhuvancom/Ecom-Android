package com.tcs.ecom.utility

import com.tcs.ecom.models.ApiError

sealed class ApiResultState<out T> {
    object LOADING : ApiResultState<Nothing>()
    object START : ApiResultState<Nothing>()
    data class SUCCESS<out T>(val result: T) : ApiResultState<T>()
    data class ERROR(val apiError: ApiError) : ApiResultState<Nothing>()
}
