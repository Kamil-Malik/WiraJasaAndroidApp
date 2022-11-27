package com.wirajasa.wirajasabisnis.presentation.service

import com.google.firebase.firestore.PropertyName

data class ServicePost(
    var uid: String = "",
    var name: String = "",
    var price: Int = 0,
    var unit: String = "",
    var address: String = "",
    var Email: String = "",
    @get:PropertyName("phone_number") @set:PropertyName("phone_number") var phoneNumber: String = "",
    @get:PropertyName("photo_url") @set:PropertyName("photo_url") var photoUrl: String = ""
)
