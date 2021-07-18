package com.tcs.ecom.ui.main.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentSettingBinding

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    7:37 PM
Project Ecom
 */
class SettingFragment : Fragment(R.layout.fragment_setting) {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingBinding.bind(view)
    }
}