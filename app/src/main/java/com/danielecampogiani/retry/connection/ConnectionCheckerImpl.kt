package com.danielecampogiani.retry.connection

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

class ConnectionCheckerImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    application: Application
) : ConnectionChecker {

    private val listeners = CopyOnWriteArrayList<ConnectionChecker.Listener>()

    init {
        val networkReceiver = NetworkReceiver { onNetworkChanged() }
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        application.registerReceiver(networkReceiver, filter)
    }

    override fun addListener(listener: ConnectionChecker.Listener) {
        listeners.addIfAbsent(listener)
    }

    override fun removeListener(listener: ConnectionChecker.Listener) {
        listeners.remove(listener)
    }

    private fun onNetworkChanged() {
        val connected = isConnected
        listeners.forEach {
            it.onConnectionChanged(connected)
        }

    }

    override val isConnected: Boolean
        get() {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetwork?.isConnected ?: false
        }
}

private class NetworkReceiver(
    private val onReceive: () -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        onReceive()
    }
}