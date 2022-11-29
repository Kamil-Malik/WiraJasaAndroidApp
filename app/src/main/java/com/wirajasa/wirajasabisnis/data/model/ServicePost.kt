package com.wirajasa.wirajasabisnis.data.model

import com.google.firebase.firestore.PropertyName

data class ServicePost(
    var uid: String = "",
    var name: String = "",
    var price: Int = 0,
    var unit: String = "",
    var address: String = "",
    var province: String = "",
    @get:PropertyName(PHONE_NUMBER) @set:PropertyName(PHONE_NUMBER) var phoneNumber: String = "",
    @get:PropertyName(PHOTO) @set:PropertyName(PHOTO) var photoUrl: String = ""
){
    companion object{
        private const val PHOTO = "photo_url"
        private const val PHONE_NUMBER = "phone_number"
    }
}
