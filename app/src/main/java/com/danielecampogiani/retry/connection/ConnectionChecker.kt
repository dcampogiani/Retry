package com.danielecampogiani.retry.connection

interface ConnectionChecker {

    val isConnected: Boolean

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {
        fun onConnectionChanged(hasConnection: Boolean)
    }
}