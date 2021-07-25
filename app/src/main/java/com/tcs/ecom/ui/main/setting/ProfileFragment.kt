package com.tcs.ecom.ui.main.setting

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tcs.ecom.R
import com.tcs.ecom.databinding.BottomSheetProfileBinding
import com.tcs.ecom.ui.auth.AuthenticationViewModel
import com.tcs.ecom.utility.ApiResultState
import com.tcs.ecom.utility.Constants
import com.tcs.ecom.utility.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/**
@author Bhuvaneshvar
Date    7/24/2021
Time    4:47 PM
Project Ecom
 */
@AndroidEntryPoint
class ProfileFragment : BottomSheetDialogFragment() {
    private var binding: BottomSheetProfileBinding? = null
    private val args by navArgs<ProfileFragmentArgs>()
    private val authViewModel by viewModels<AuthenticationViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = BottomSheetProfileBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.updateState.collectLatest { apiState ->
                    binding?.let {
                        it.progressCircular.isVisible = apiState is ApiResultState.LOADING
                        it.btnUpdate.isEnabled = apiState !is ApiResultState.LOADING
                    }
                    when (apiState) {
                        is ApiResultState.ERROR -> {
                            Util.showAlert(
                                requireContext(),
                                onYes = {
                                    handleUpdate()
                                },
                                onNo = {

                                },
                                "Error Updating",
                                apiState.apiError.reason + "\nRetry?"
                            )
                        }
                        is ApiResultState.LOADING -> {
                            binding?.let {
                                it.progressCircular.isVisible = true
                                it.btnUpdate.isEnabled = false
                            }
                        }
                        is ApiResultState.START -> {

                        }
                        is ApiResultState.SUCCESS -> {
                            Toast.makeText(requireContext(), "Update successful", Toast.LENGTH_LONG)
                                .show()
                            Constants.CURRENT_USER.value = apiState.result
                            dismiss()
                        }
                    }

                }
            }
        }

        binding?.apply {
            btnCancel.setOnClickListener {
                dismiss()
            }
            tvEmail.text = args.user.email
            etAddress.editText!!.setText(args.user.address)

            animateTo(etAddress, checkboxAddress.isChecked)
            animateTo(etNewPassword, checkboxPassword.isChecked)

            checkboxAddress.setOnCheckedChangeListener { _, isChecked ->
                animateTo(etAddress, isChecked)
            }

            checkboxPassword.setOnCheckedChangeListener { _, isChecked ->
                animateTo(etNewPassword, isChecked)
            }

            btnUpdate.setOnClickListener {
                handleUpdate()
            }
        }
    }

    private fun animateTo(view: View, isHiding: Boolean = false) {
        view.apply {
            if (!isHiding) {
                animate()
                    .alpha(0f)
                    .setDuration(300L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            isVisible = false
                        }
                    })
            } else {
                alpha = 0f
                isVisible = true
                animate()
                    .alpha(1f)
                    .setDuration(300L)
                    .setListener(null)
            }
        }
    }

    private fun handleUpdate() {
        binding?.apply {
            val currentPassword = etCurrentPassword.editText!!.text.toString().trim()
            if (currentPassword.isBlank()) {
                Util.showToast(requireContext(), "Please enter current password")
                return
            }
            var user = args.user
            when {
                checkboxAddress.isChecked && checkboxPassword.isChecked -> {
                    val newPassword = etNewPassword.editText!!.text.toString().trim()
                    val address = etAddress.editText!!.text.toString().trim()
                    if (newPassword.isBlank() || address.isBlank()) {
                        Util.showToast(requireContext(), "Please enter new password/address")
                        return
                    }
                    user = user.copy(address = address, password = newPassword)
                }
                checkboxPassword.isChecked -> {
                    val newPassword = etNewPassword.editText!!.text.toString().trim()
                    if (newPassword.isBlank()) {
                        Util.showToast(requireContext(), "Please enter new password")
                        return
                    }
                    user = user.copy(password = newPassword)
                }
                checkboxAddress.isChecked -> {
                    val address = etAddress.editText!!.text.toString().trim()
                    if (address.isBlank()) {
                        Util.showToast(requireContext(), "Address can not be empty")
                        return
                    }
                    user = user.copy(address = address, password = currentPassword)
                }
                else -> {
                    Util.showToast(requireContext(), "Nothing to update")
                    dismiss()
                }
            }

            authViewModel.updateUserDetails(user, currentPassword)

            Log.d(TAG, "handleUpdate: new user $user")
        }
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}