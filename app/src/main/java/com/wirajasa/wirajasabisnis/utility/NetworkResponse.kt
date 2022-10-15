package com.wirajasa.wirajasabisnis.utility

sealed class NetworkResponse<out T> {
    object Loading : NetworkResponse<Nothing>()
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class GenericException(val code: Int?, val cause: String?) : NetworkResponse<Nothing>()
    data class FirebaseException(val e: Exception) : NetworkResponse<Nothing>()
    object NetworkException : NetworkResponse<Nothing>()
}
