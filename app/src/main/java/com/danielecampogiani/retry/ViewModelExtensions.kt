package androidx.lifecycle

import com.danielecampogiani.retry.connection.ConnectionLiveData
import kotlinx.coroutines.launch

fun <T> ViewModel.launchWithRetry(
    mediatorLiveData: MediatorLiveData<*>,
    networkOperation: suspend () -> T,
    loading: () -> Unit,
    resultConsumer: (T) -> Unit,
    onNoNetwork: () -> Unit,
    connectionLiveData: ConnectionLiveData
) {
    mediatorLiveData.addSource(connectionLiveData) { hasConnection ->
        loading()
        if (hasConnection) {
            viewModelScope.launch {
                val networkData = networkOperation()
                resultConsumer(networkData)
            }

        } else {
            onNoNetwork()
        }
    }

}