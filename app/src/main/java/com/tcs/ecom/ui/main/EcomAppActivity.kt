package com.tcs.ecom.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.tcs.ecom.R
import com.tcs.ecom.databinding.ActivityEcomAppBinding
import com.tcs.ecom.ui.main.cart.CartViewModel
import com.tcs.ecom.utility.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
    private val cartViewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEcomAppBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (Constants.CURRENT_USER == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show()
            super.onBackPressed()
            return
        }

        navController = supportFragmentManager.findFragmentById(R.id.navHostFragMain)
            ?.findNavController() as NavHostController
        val appBar = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBar)
        binding.mainBottomBar.setupWithNavController(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                cartViewModel.getUserCart(Constants.CURRENT_USER!!.id!!)
            }
        }

        Constants.CUURENT_CART.observe(this, {
            updateCartBadge(it.numberOfProducts.toInt())
        })

    }

    companion object {
        private const val TAG = "EcomAppActivity"
    }

    private fun updateCartBadge(nmbr: Int) {
        binding.mainBottomBar.getOrCreateBadge(R.id.cartFragment).number = nmbr
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

}