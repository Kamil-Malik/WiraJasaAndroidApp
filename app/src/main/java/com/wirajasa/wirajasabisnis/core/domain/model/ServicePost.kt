package com.wirajasa.wirajasabisnis.core.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServicePost(
    var uid: String = "",
    @get:PropertyName(SERVICE_ID) @set:PropertyName(SERVICE_ID) var serviceId: String = "",
    var name: String = "",
    var price: Int = 0,
    var unit: String = "",
    var address: String = "",
    var province: String = "",
    @get:PropertyName(PHONE_NUMBER) @set:PropertyName(PHONE_NUMBER) var phoneNumber: String = "",
    @get:PropertyName(PHOTO) @set:PropertyName(PHOTO) var photoUrl: String = ""
) : Parcelable{
    companion object{
        private const val PHOTO = "photo_url"
        private const val PHONE_NUMBER = "phone_number"
        private const val SERVICE_ID = "service_id"
    }
}
