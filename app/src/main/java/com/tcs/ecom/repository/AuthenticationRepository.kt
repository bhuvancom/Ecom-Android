package com.tcs.ecom.repository

import com.tcs.ecom.api.AuthenticationApi
import com.tcs.ecom.models.Users
import javax.inject.Inject

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    3:36 PM
Project Ecom
 */
class AuthenticationRepository @Inject constructor(private val authenticationApi: AuthenticationApi) {
    suspend fun login(emailPassMap: HashMap<String, String>) = authenticationApi.login(emailPassMap)
    suspend fun register(users: Users) = authenticationApi.register(users)
}