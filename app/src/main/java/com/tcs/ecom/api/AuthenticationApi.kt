package com.tcs.ecom.api

import com.tcs.ecom.models.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
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
}