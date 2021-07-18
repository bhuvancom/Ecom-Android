package com.tcs.ecom.ui.main.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentSearchBinding

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    7:37 PM
Project Ecom
 */
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
    }
}