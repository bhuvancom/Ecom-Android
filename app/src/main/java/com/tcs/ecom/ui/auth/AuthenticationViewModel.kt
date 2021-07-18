package com.tcs.ecom.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tcs.ecom.models.Error
import com.tcs.ecom.models.Users
import com.tcs.ecom.repository.AuthenticationRepository
import com.tcs.ecom.utility.ApiResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    3:09 PM
Project Ecom
 */
@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    ViewModel() {

    private val _loginState = MutableStateFlow<ApiResultState<Users>>(ApiResultState.START)
    val loginState: StateFlow<ApiResultState<Users>> = _loginState

    private val _registrationState = MutableStateFlow<ApiResultState<Users>>(ApiResultState.START)
    val registrationState: StateFlow<ApiResultState<Users>> get() = _registrationState

    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            _loginState.emit(ApiResultState.LOADING)
            val map = HashMap<String, String>()
            map["email"] = email
            map["password"] = password
            try {
                val login = authenticationRepository.login(map)
                if (login.isSuccessful && login.body() != null) {
                    val user = login.body()!!
                    user.password = password
                    _loginState.tryEmit(ApiResultState.SUCCESS(user))

                } else {
                    login.errorBody()?.apply {
                        try {
                            val gson = Gson()
                            val err = this.string()
                            val error = gson.fromJson(err, Error::class.java)
                            _loginState.tryEmit(ApiResultState.ERROR(error))
                        } catch (e: Exception) {
                            Log.e(TAG, "doLogin: converting to error -> error $e")
                            _loginState.tryEmit(
                                ApiResultState.ERROR(
                                    Error(
                                        "Please check you email or password",
                                        500
                                    )
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "doLogin: $e")
                _loginState.emit(ApiResultState.ERROR(Error("${e.message}", 500)))
            }

        }
    }


    fun doRegister(users: Users) {
        viewModelScope.launch {
            _registrationState.tryEmit(ApiResultState.LOADING)
            try {
                val response = authenticationRepository.register(users)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    user.password = users.password

                    _registrationState.tryEmit(ApiResultState.SUCCESS(user))

                } else {
                    response.errorBody()?.apply {
                        try {
                            val gson = Gson()
                            val err = this.string()
                            val error = gson.fromJson(err, Error::class.java)
                            _registrationState.tryEmit(ApiResultState.ERROR(error))
                        } catch (e: Exception) {
                            Log.e(TAG, "doLogin: converting to error -> error $e")
                            _registrationState.tryEmit(
                                ApiResultState.ERROR(
                                    Error(
                                        "Please check you email or password",
                                        500
                                    )
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "doLogin: $e")
                _registrationState.emit(ApiResultState.ERROR(Error("${e.message}", 500)))
            }
        }
    }
}


private const val TAG = "AuthenticationViewModel"