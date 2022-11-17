package com.wirajasa.wirajasabisnis.utility

sealed class NetworkResponse<out T> {
    object Loading : NetworkResponse<Nothing>()
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class GenericException(val cause: String?) : NetworkResponse<Nothing>()
}
