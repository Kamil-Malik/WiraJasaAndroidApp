package com.wirajasa.wirajasabisnis.data.model

data class UserProfile(
    val uid: String,
    val username: String,
    val address: String,
    val phone_number: String,
    val seller: Boolean
)
