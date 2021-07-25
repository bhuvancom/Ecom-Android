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
