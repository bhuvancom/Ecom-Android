package com.tcs.ecom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.tcs.ecom.models.Users
import com.tcs.ecom.ui.auth.AuthenticationActivity
import com.tcs.ecom.ui.auth.AuthenticationViewModel
import com.tcs.ecom.ui.main.EcomAppActivity
import com.tcs.ecom.utility.ApiResultState
import com.tcs.ecom.utility.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val authViewModel
            by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(Constants.MY_SHARED_PREF, Context.MODE_PRIVATE)
        val userHandle = sharedPreferences.getString(Constants.USER_DETAIL, "")
        if (userHandle != null && userHandle.isNotBlank()) {
            val gson = Gson()
            try {
                val user = gson.fromJson(userHandle, Users::class.java)
                checkIfLoginIsCorrect(user)
            } catch (e: Exception) {
                Log.e(TAG, "onCreate: error getting user $e")
                openAuth()
            }
        } else {
            openAuth()
        }
    }

    private fun checkIfLoginIsCorrect(user: Users?) {
        if (user == null) {
            openAuth()
            return
        }

        authViewModel.doLogin(user.email, user.password)
        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                authViewModel.loginState.collectLatest {
                    when (it) {
                        is ApiResultState.SUCCESS -> {
                            Constants.CURRENT_USER.postValue(it.result)
                            openMain()
                        }
                        is ApiResultState.ERROR -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Error ${it.apiError.reason}",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            openAuth()
                        }
                        is ApiResultState.LOADING -> {

                        }
                        is ApiResultState.START -> {

                        }
                    }
                }
            }
        }
    }

    private fun openMain() {
        val intent = Intent(this, EcomAppActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun openAuth() {

        val sharedPreferences = getSharedPreferences(Constants.MY_SHARED_PREF, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            this.remove(Constants.USER_DETAIL)
            commit()
        }
        Constants.CURRENT_USER.postValue(null)

        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}