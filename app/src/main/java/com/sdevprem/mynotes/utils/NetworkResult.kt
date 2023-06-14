package com.sdevprem.mynotes.utils

sealed class NetworkResult<out T> {
    class Success<out T>(val data: T) : NetworkResult<T>()
    class Error(val msg: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
    object Idle : NetworkResult<Nothing>()
}