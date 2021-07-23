package com.tcs.ecom.ui.main.setting

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
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
class SettingsAdapter(private val onClick: (Screen) -> Unit) :
    ListAdapter<SettingsModel, SettingsAdapter.SettingViewHolder>(diifUtil) {

    inner class SettingViewHolder(val binding: ItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(settingsModel: SettingsModel) {
            binding.tvTitle.text = settingsModel.title
            binding.tvSubtitle.text = settingsModel.subTitle
            Log.d(TAG, "bind: $adapterPosition")
            if (adapterPosition != RecyclerView.NO_POSITION) {
                binding.root.setOnClickListener {
                    Log.d(TAG, "setting:click ")
                    onClick(getItem(bindingAdapterPosition).screen)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val binding = ItemSettingsBinding.inflate(LayoutInflater.from(parent.context))
        return SettingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diifUtil = object : DiffUtil.ItemCallback<SettingsModel>() {
            override fun areItemsTheSame(oldItem: SettingsModel, newItem: SettingsModel): Boolean =
                oldItem.screen == newItem.screen

            override fun areContentsTheSame(
                oldItem: SettingsModel,
                newItem: SettingsModel
            ): Boolean = oldItem == newItem
        }

        private const val TAG = "SettingsAdapter"
    }
}