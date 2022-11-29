package com.wirajasa.wirajasabisnis.data.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SellerApplication(

    @get:PropertyName(UserProfile.USERID) @set:PropertyName(UserProfile.USERID)
    var uid: String = "",

    @get:PropertyName(APPLICATION_ID) @set:PropertyName(APPLICATION_ID)
    var applicationId: String = "",

    @get:PropertyName(FULL_NAME) @set:PropertyName(FULL_NAME)
    var fullName: String = "",

    @get:PropertyName(FULL_ADDRESS) @set:PropertyName(FULL_ADDRESS)
    var address: String = "",

    @get:PropertyName(FULL_PHONE_NUMBER) @set:PropertyName(FULL_PHONE_NUMBER)
    var phoneNumber: String = "",

    @get:PropertyName(PHOTO_ID) @set:PropertyName(PHOTO_ID)
    var photoID: String = "",

    @get:PropertyName(PROVINCE) @set:PropertyName(PROVINCE)
    var province: String = "",

    @get:PropertyName(APPLICATION_STATUS) @set:PropertyName(APPLICATION_STATUS)
    var applicationStatus: String = PENDING
) : Parcelable {
    companion object {
        private const val APPLICATION_ID = "application_id"
        private const val FULL_NAME = "full_name"
        private const val FULL_ADDRESS = "full_address"
        private const val FULL_PHONE_NUMBER = "full_phone_number"
        private const val PHOTO_ID = "id_photo"
        private const val PROVINCE = "province"
        private const val APPLICATION_STATUS = "application_status"
        private const val PENDING = "pending"
    }
}
