package com.tcs.ecom.ui.main.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentSettingBinding
import com.tcs.ecom.models.Screen
import com.tcs.ecom.models.SettingsModel

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    7:37 PM
Project Ecom
 */
class SettingFragment : Fragment(R.layout.fragment_setting) {
    companion object {
        private const val TAG = "SettingFragment"
    }

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val settingsAdapter by lazy {
        SettingsAdapter(onClick = {
            handleScreen(it)
        })
    }

    private fun handleScreen(screen: Screen) {
        Log.d(TAG, "handleScreen: $screen")
        if (screen == Screen.ORDERS) {
            findNavController().navigate(R.id.action_settingFragment_to_orderFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingBinding.bind(view)

        setupRecyclerView()

        settingsAdapter.submitList(
            listOf(
                SettingsModel(
                    "Orders",
                    "Tap to open your orders",
                    Screen.ORDERS
                ),
                SettingsModel(
                    "Orders",
                    "Tap to open your orders",
                    Screen.ORDERS
                ),
                SettingsModel(
                    "Orders",
                    "Tap to open your orders",
                    Screen.ORDERS
                ),
                SettingsModel(
                    "Profile",
                    "Tap to open your profile",
                    Screen.PROFILE
                ),
            )
        )

    }

    private fun setupRecyclerView() {
        binding.rvSettings.apply {
            adapter = settingsAdapter
            layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }
}