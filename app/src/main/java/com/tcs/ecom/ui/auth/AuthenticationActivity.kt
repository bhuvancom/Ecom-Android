package com.tcs.ecom.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.tcs.ecom.R
import com.tcs.ecom.databinding.ActivityAuchenticationBinding
import com.tcs.ecom.models.Users
import com.tcs.ecom.ui.main.EcomAppActivity
import com.tcs.ecom.utility.Constants
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
        val sharedPreferences = getSharedPreferences(Constants.MY_SHARED_PREF, Context.MODE_PRIVATE)
        val gson = Gson()
        val user = gson.toJson(users).toString()
        sharedPreferences.edit {
            putString(Constants.USER_DETAIL, user)
            commit()
        }
        Constants.CURRENT_USER = users
        val intent = Intent(this, EcomAppActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}