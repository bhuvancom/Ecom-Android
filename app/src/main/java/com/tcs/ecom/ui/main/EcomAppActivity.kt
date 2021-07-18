package com.tcs.ecom.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.tcs.ecom.R
import com.tcs.ecom.databinding.ActivityEcomAppBinding
import dagger.hilt.android.AndroidEntryPoint

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    7:09 PM
Project Ecom
 */
@AndroidEntryPoint
class EcomAppActivity : AppCompatActivity() {
    private var _binding: ActivityEcomAppBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEcomAppBinding.inflate(layoutInflater)

        setContentView(binding.root)

        navController = supportFragmentManager.findFragmentById(R.id.navHostFragMain)
            ?.findNavController() as NavHostController
        val appBar = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBar)
        binding.mainBottomBar.setupWithNavController(navController)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

}