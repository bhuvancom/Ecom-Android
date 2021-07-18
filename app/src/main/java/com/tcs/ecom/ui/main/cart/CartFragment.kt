package com.tcs.ecom.ui.main.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tcs.ecom.R
import com.tcs.ecom.databinding.FragmentCartBinding

/**
@author Bhuvaneshvar
Date    7/18/2021
Time    7:37 PM
Project Ecom
 */
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
    }
}