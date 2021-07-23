package com.tcs.ecom

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import android.util.Log
import com.tcs.ecom.utility.Constants
import dagger.hilt.android.HiltAndroidApp


/**
@author Bhuvaneshvar
Date    7/18/2021
Time    3:15 PM
Project Ecom
 */
@HiltAndroidApp
class EcomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        if (wifiManager != null && wifiManager.isWifiEnabled) {
            ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        }

        Log.d(TAG, "onCreate: $ip")
    }

    companion object {
        var ip = ""

        private const val TAG = "EcomApplication"
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID,
                Constants.CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "High importance notification"
            val channel2 = NotificationChannel(
                Constants.CHANNEL_ID_WITHOUT_SOUND,
                Constants.CHANNEL_ID_WITHOUT_SOUND,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel2.description = "Medium importance notification"
            val channel3 = NotificationChannel(
                Constants.CHANNEL_ID_STICKY,
                Constants.CHANNEL_ID_STICKY,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel3.description = "High importance notification"
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel)
            manager.createNotificationChannel(channel2)
            manager.createNotificationChannel(channel3)
        }
    }
}