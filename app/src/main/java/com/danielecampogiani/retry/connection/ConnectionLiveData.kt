package com.danielecampogiani.retry.connection

import androidx.lifecycle.LiveData
import javax.inject.Inject

class ConnectionLiveData @Inject constructor(
    private val connectionChecker: ConnectionChecker
) : LiveData<Boolean>() {

    private val listener = object : ConnectionChecker.Listener {
        override fun onConnectionChanged(hasConnection: Boolean) {
            value = hasConnection
        }

    }

    override fun onActive() {
        connectionChecker.addListener(listener)
    }

    override fun onInactive() {
        connectionChecker.removeListener(listener)
    }
}