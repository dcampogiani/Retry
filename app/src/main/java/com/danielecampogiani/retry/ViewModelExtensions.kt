package androidx.lifecycle

import com.danielecampogiani.retry.connection.ConnectionChecker
import kotlinx.coroutines.launch
import java.io.Closeable

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
        val listenerHashCode = listener.hashCode().toString()
        setTagIfAbsent(listenerHashCode, object : Closeable {
            override fun close() {
                connectionChecker.removeListener(listener)
            }
        })
    }

}