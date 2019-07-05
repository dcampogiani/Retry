package com.danielecampogiani.retry.detail

sealed class DetailState {

    data class Result(val value: String) : DetailState()
    data class Error(val errorId: Int) : DetailState()
    object Loading : DetailState()

    fun <T> fold(result: (Result) -> T, error: (Error) -> T, loading: (Loading) -> T): T {
        return when (this) {
            is Result -> result(this)
            is Error -> error(this)
            Loading -> loading(Loading)
        }
    }
}