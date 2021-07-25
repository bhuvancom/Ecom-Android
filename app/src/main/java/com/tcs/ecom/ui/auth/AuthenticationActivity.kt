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
package com.tcs.ecom.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tcs.ecom.R
import com.tcs.ecom.databinding.ActivityAuchenticationBinding
import com.tcs.ecom.models.Users
import com.tcs.ecom.ui.main.EcomAppActivity
import com.tcs.ecom.utility.Constants
import com.tcs.ecom.utility.Util
import dagger.hilt.android.AndroidEntryPoint

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    2:55 PM
Project Ecom
 */
private const val TAG = "AuthenticationActivity"

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuchenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: ")
        val controller = supportFragmentManager.findFragmentById(R.id.navHostFragAuth)
            ?.findNavController() as NavHostController

        binding.authBottomBar.setupWithNavController(controller)
    }

    fun modeToMainApp(users: Users) {
        Util.addUserInSharedPref(this, users)
        Constants.CURRENT_USER.postValue(users)
        val intent = Intent(this, EcomAppActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
