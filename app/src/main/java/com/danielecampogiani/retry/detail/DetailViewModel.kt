package com.danielecampogiani.retry.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.launchWithRetry
import com.danielecampogiani.retry.R
import com.danielecampogiani.retry.connection.ConnectionLiveData
import javax.inject.Inject

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
