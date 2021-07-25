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
package com.tcs.ecom.utility

import com.tcs.ecom.models.ApiError

sealed class ApiResultState<out T> {
    object LOADING : ApiResultState<Nothing>()
    object START : ApiResultState<Nothing>()
    data class SUCCESS<out T>(val result: T) : ApiResultState<T>()
    data class ERROR(val apiError: ApiError) : ApiResultState<Nothing>()
}
