package com.example.weatherphoto.domain.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.LiveData

class InternetConnectivity (val context: Context) : LiveData<Boolean>() {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var connectServer: Boolean? = false
    private val receiveConnectivityManager = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            connectServer=false
            updateConnection()
            postValue(connectServer)
        }
    }
    init {
        updateConnection()
    }

    private fun updateConnection(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities?.run {
                connectServer= capabilities.hasTransport(TRANSPORT_CELLULAR) || capabilities.hasTransport(TRANSPORT_WIFI)
            }
        } else {
            try {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                connectServer = activeNetworkInfo != null && activeNetworkInfo.isConnected
            } catch (e: Exception) {
            }
        }
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(
            receiveConnectivityManager,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(receiveConnectivityManager)
    }
}