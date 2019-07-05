package com.danielecampogiani.retry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielecampogiani.retry.connection.ConnectionChecker
import kotlinx.coroutines.launch

fun <T> ViewModel.launchWithRetry(
    networkOperation: suspend () -> T,
    loading: () -> Unit,
    resultConsumer: (T) -> Unit,
    onNoNetwork: () -> Unit,
    connectionChecker: ConnectionChecker
) {

    if (connectionChecker.isConnected) {
        loading()
        viewModelScope.launch {
            val networkData = networkOperation()
            resultConsumer(networkData)
        }
    } else {
        onNoNetwork()
        val listener = object : ConnectionChecker.Listener {
            override fun onConnected() {
                connectionChecker.removeListener(this)
                launchWithRetry(networkOperation, loading, resultConsumer, onNoNetwork, connectionChecker)
            }
        }
        connectionChecker.addListener(listener)
    }

}