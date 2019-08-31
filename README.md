# Retry
Automatic retry of network requests based on Coroutines and Viewmodel

<img src="https://github.com/dcampogiani/Retry/blob/master/demo.gif?raw=true" width="250"> 

# How
Core code is in ViewModelExtensions.kt: 

```kotlin
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
```

ConnectionLiveData is a custom LiveData used to observe connectivity changes.
We add it as a source to our MediatorLiveData, this way we can react to connectivity changes. 
Every time we receive new data if the device is currently connected we use the viewModelScope to perform the network request and notify the consumer, otherwise, we notify a "no network" listener.

# Usage

From any viewModel usage is : 

```kotlin
class DetailViewModel @Inject constructor(
    private val service: DetailService,
    connectionLiveData: ConnectionLiveData
) : ViewModel() {

    private val mutableState = MediatorLiveData<DetailState>()
    val state: LiveData<DetailState>
        get() = mutableState


    init {
        launchWithRetry(
            mediatorLiveData = mutableState,
            networkOperation = { service.loadData() },
            loading = { mutableState.value = DetailState.Loading },
            resultConsumer = { mutableState.value = DetailState.Result(it) },
            onNoNetwork = { mutableState.value = DetailState.Error(R.string.no_network_message) },
            connectionLiveData = connectionLiveData
        )
    }
}
```

