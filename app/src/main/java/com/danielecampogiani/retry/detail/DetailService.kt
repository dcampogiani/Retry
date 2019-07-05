package com.danielecampogiani.retry.detail

interface DetailService {

    suspend fun loadData(): String
}