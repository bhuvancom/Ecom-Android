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
import com.tcs.ecom.databinding.FragmentRegistrationBinding
import com.tcs.ecom.models.Users
import com.tcs.ecom.utility.ApiResultState
import com.tcs.ecom.utility.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    3:02 PM
Project Ecom
 */
private const val TAG = "RegistrationFragment"

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private val authViewModel by viewModels<AuthenticationViewModel>()
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegistrationBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            doRegister()
        }

        lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                authViewModel.registrationState.collect {
                    binding.btnRegister.isEnabled = it !is ApiResultState.LOADING
                    binding.progressCircular.isVisible = it is ApiResultState.LOADING

                    when (it) {
                        is ApiResultState.LOADING -> {
                            Log.d(TAG, "onViewCreated: loading")
                        }

                        is ApiResultState.ERROR -> {
                            Log.d(TAG, "onViewCreated: ${it.apiError}")
                            Util.showAlert(
                                requireContext(),
                                onYes = {
                                    doRegister()
                                },
                                onNo = {},
                                "Error Registering",
                                it.apiError.reason + "\nRetry?"
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

    private fun doRegister() {
        val email = binding.etEmail.editText?.text.toString()
        val password = binding.etPassword.editText?.text.toString()
        val address = binding.etDefaultAddress.editText?.text.toString()
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

        if (address.isBlank()) {
            binding.etDefaultAddress.error = "Address is required"
            return
        }
        binding.etDefaultAddress.isErrorEnabled = false

        val user = Users(null, email, password, address)

        authViewModel.doRegister(user)

    }
}
