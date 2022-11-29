package com.wirajasa.wirajasabisnis.core.utility

sealed class NetworkResponse<out T> {
    data class Loading(val status: String?) : NetworkResponse<Nothing>()
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class GenericException(val cause: String?) : NetworkResponse<Nothing>()
}
