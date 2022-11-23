package com.wirajasa.wirajasabisnis.data.model

data class UserProfile(
    val uid: String,
    val username: String,
    val address: String,
    val phone_number: String,
    val photourl: String,
    val seller: Boolean,
    val createdAt: String
) {
    /*  Tolong jangan didelete constructor ini, karena untuk convert snapshot
    kedalam dataclass secara otomatis l*/

    constructor() : this(
        "", "", "", "", "", false, ""
    )
}