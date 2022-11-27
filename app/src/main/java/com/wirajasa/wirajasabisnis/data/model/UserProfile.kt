package com.wirajasa.wirajasabisnis.data.model

import com.google.firebase.firestore.PropertyName

data class UserProfile(

    @get:PropertyName(USERID) @set:PropertyName(USERID) var uid: String = "",
    var username: String = "",

    var address: String = "",

    @get:PropertyName(PHONE_NUMBER) @set:PropertyName(PHONE_NUMBER)
    var phone_number: String = "",

    @get:PropertyName(PHOTO) @set:PropertyName(PHOTO)
    var image: String = "",

    @get:PropertyName(SELLER) @set:PropertyName(SELLER)
    var isSeller: Boolean = false,

    @get:PropertyName(VERIFIED) @set:PropertyName(VERIFIED)
    var isVerified: Boolean = false,

    @get:PropertyName(ADMIN) @set:PropertyName(ADMIN)
    var isAdmin: Boolean = false

) {
    companion object {
        const val USERID = "id"
        private const val PHOTO = "photo_url"
        private const val PHONE_NUMBER = "phone_number"
        private const val SELLER = "seller"
        private const val VERIFIED = "verified"
        private const val ADMIN = "admin"
    }
}