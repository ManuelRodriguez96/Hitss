package com.hitss.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

/**
 * Network class checks the device's network connectivity.
 * This class implements the NetworkConnectivity interface and uses the
 * ConnectivityManager system service to determine if the device is connected to a network.
 * @property context The application context used to access system services.
 */
class Network @Inject constructor(private val context: Context) : NetworkConnectivity {
    override fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities.also {
            if (it != null) {
                if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true
                else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                }
            }
        }
        return false
    }
}

interface NetworkConnectivity {
    fun isConnected(): Boolean
}