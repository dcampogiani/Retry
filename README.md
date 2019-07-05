# Retry
Automatic retry of network requests based on Coroutines and Viewmodel

<img src="https://github.com/dcampogiani/Retry/blob/master/demo.gif?raw=true" width="250"> 

# How
Core code is in ViewModelExtensions.kt: 

```kotlin
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
```

If the device is currently connected we use the viewModelScope to perform the network request and notify the consumer.
Otherwise, we register a listener to schedule the network request once the device will gain connectivity again.

# Usage

From any viewModel usage is : 

```kotlin
class DetailViewModel @Inject constructor(
    private val connectionChecker: ConnectionChecker,
    private val service: DetailService
) : ViewModel() {

    private val mutableState = MutableLiveData<DetailState>()

    val state: LiveData<DetailState>
        get() = mutableState


    init {
        load()
    }

    private fun load() {
        launchWithRetry(
            networkOperation = { service.loadData() },
            resultConsumer = { mutableState.value = DetailState.Result(it) },
            loading = { mutableState.value = DetailState.Loading },
            onNoNetwork = { mutableState.value = DetailState.Error(R.string.no_network_message) },
            connectionChecker = connectionChecker
        )
    }
}
```

