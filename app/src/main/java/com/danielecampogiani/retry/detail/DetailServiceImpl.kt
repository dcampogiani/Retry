package com.danielecampogiani.retry.detail

import com.danielecampogiani.retry.detail.api.JokesAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailServiceImpl @Inject constructor(
    private val api: JokesAPI
) : DetailService {

    override suspend fun loadData(): String {
        return withContext(Dispatchers.IO) {
            api.getJoke().execute().body()!!.value.joke
        }
    }
}