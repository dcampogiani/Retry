package com.danielecampogiani.retry.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.launchWithRetry
import com.danielecampogiani.retry.R
import com.danielecampogiani.retry.connection.ConnectionChecker
import javax.inject.Inject

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
