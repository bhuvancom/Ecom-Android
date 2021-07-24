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