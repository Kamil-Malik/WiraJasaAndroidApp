package com.wirajasa.wirajasabisnis.data.model

import com.google.firebase.firestore.PropertyName

data class UserProfile(
    @get:PropertyName(USERID) @set:PropertyName(USERID) var uid: String = "",
    var username: String = "",
    var address: String = "",
    @get:PropertyName(PHONE_NUMBER) @set:PropertyName(PHONE_NUMBER) var phone_number: String = "",
    @get:PropertyName(PHOTO) @set:PropertyName(PHOTO) var image: String = "",
    var seller: Boolean = false,
    @get:PropertyName(CREATED_AT) @set:PropertyName(CREATED_AT) var createdAt: String = ""
) {
    companion object {
        private const val USERID = "id"
        private const val PHOTO = "photo_url"
        private const val PHONE_NUMBER = "phone_number"
        private const val CREATED_AT = "created_at"
    }
}