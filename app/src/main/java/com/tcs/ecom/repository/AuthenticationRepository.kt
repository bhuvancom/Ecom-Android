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
    suspend fun update(users: Users, oldPass: String) =
        authenticationApi.update(users, oldPass)
}
