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
package com.tcs.ecom.api

import com.tcs.ecom.models.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    3:27 PM
Project Ecom
 */
interface AuthenticationApi {

    @POST("users/login")
    suspend fun login(@QueryMap emailAndPassword: HashMap<String, String>): Response<Users>

    @POST("users")
    suspend fun register(@Body users: Users): Response<Users>

    @POST("users/update")
    suspend fun update(
        @Body users: Users,
        @Query("old_password") oldPass: String
    ): Response<Users>
}
