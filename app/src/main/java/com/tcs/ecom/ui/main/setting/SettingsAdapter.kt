/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tcs.ecom.ui.main.setting

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcs.ecom.databinding.ItemSettingsBinding
import com.tcs.ecom.models.Screen
import com.tcs.ecom.models.SettingsModel

/**
@author Bhuvaneshvar
Date    7/23/2021
Time    10:01 PM
Project Ecom
 */
class SettingsAdapter(
    private inline val onClick: (Screen) -> Unit,
    private val options: List<SettingsModel>
) :
    RecyclerView.Adapter<SettingsAdapter.SettingViewHolder>() {

    inner class SettingViewHolder(val binding: ItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            Log.d(TAG, "pos:$absoluteAdapterPosition ")
            if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                binding.root.setOnClickListener {
                    onClick(options[bindingAdapterPosition].screen)
                }
            }
        }

        fun bind(settingsModel: SettingsModel) {
            binding.tvTitle.text = settingsModel.title
            binding.tvSubtitle.text = settingsModel.subTitle
            binding.root.setOnClickListener {
                onClick(settingsModel.screen)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val binding = ItemSettingsBinding.inflate(LayoutInflater.from(parent.context))
        return SettingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        holder.bind(options[position])
    }

    companion object {
        private const val TAG = "SettingsAdapter"
    }

    override fun getItemCount(): Int = options.size
}
