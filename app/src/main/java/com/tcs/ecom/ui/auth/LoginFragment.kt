package com.tcs.ecom.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentLoginBinding
import com.tcs.ecom.utility.ApiResultState
import com.tcs.ecom.utility.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    3:01 PM
Project Ecom
 */
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthenticationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                authViewModel.loginState.collectLatest {
                    binding.btnLogin.isEnabled = it !is ApiResultState.LOADING
                    binding.progressCircular.isVisible = it is ApiResultState.LOADING

                    when (it) {
                        is ApiResultState.LOADING -> {
                            Log.d(TAG, "onViewCreated: loading")
                        }

                        is ApiResultState.ERROR -> {
                            Log.d(TAG, "onViewCreated: ${it.error}")
                            Util.showAlert(
                                requireContext(),
                                onYes = {
                                    doLogin()
                                },
                                onNo = {},
                                "Error Login",
                                it.error.reason + "\nRetry?"
                            )
                        }

                        is ApiResultState.SUCCESS -> {
                            Log.d(TAG, "onViewCreated: ${it.result}")
                            (activity as AuthenticationActivity).modeToMainApp(it.result)
                        }
                        ApiResultState.START -> {

                        }
                    }
                }
            }
        }
    }

    private fun doLogin() {
        val email = binding.etEmail.editText?.text.toString()
        val password = binding.etPassword.editText?.text.toString()
        val regex =
            Regex(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$",
                RegexOption.IGNORE_CASE
            )
        if (email.isBlank() || !email.matches(regex)) {
            Log.d(TAG, "onViewCreated: ${email.matches(regex)}")
            binding.etEmail.error = "Please correct email"
            return
        }
        binding.etEmail.isErrorEnabled = false
        if (password.isBlank()) {
            binding.etPassword.error = "Password is required"
            return
        }
        binding.etPassword.isErrorEnabled = false

        authViewModel.doLogin(email, password)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

private const val TAG = "LoginFragment"