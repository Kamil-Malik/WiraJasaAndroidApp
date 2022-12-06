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
    @get:PropertyName(PROVINCE) @set:PropertyName(PROVINCE) var province: String = "",
    @get:PropertyName(PHONE_NUMBER) @set:PropertyName(PHONE_NUMBER) var phoneNumber: String = "",
    @get:PropertyName(PHOTO) @set:PropertyName(PHOTO) var photoUrl: String = ""
) : Parcelable {
    companion object {
        const val UID = "uid"
        const val SERVICE_ID = "service_id"
        const val NAME = "name"
        const val PRICE = "price"
        const val UNIT = "unit"
        const val ADDRESS = "address"
        const val PROVINCE = "province"
        const val PHONE_NUMBER = "phone_number"
        const val PHOTO = "photo_url"
        const val SLASH = "/"
    }
}
