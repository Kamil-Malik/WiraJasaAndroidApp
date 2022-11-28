package com.wirajasa.wirajasabisnis.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.data.model.SellerApplication
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.ADDRESS
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.ADMIN
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.APPLICATION_ID
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.APPLICATION_STATUS
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.FULL_NAME
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.PHONE_NUMBER
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.PHOTO_ID_URL
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.PHOTO_URL
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.PROVINCE
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.SELLER
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.UID
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.USERNAME
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.VERIFIED

class CryptoPref(mContext: Context) {

    private val notSetup = mContext.getString(R.string.tv_not_setup)
    private val pending = mContext.getString(R.string.tv_pending)
    private val masterKey: MasterKey = MasterKey.Builder(mContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .setUserAuthenticationRequired(false)
        .build()
    private val preferences = EncryptedSharedPreferences.create(
        mContext,
        USER_PREF,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getProfile(): UserProfile {
        return UserProfile(
            uid = preferences.getString(UID, notSetup) as String,
            username = preferences.getString(USERNAME, notSetup) as String,
            address = preferences.getString(ADDRESS, notSetup) as String,
            phone_number = preferences.getString(PHONE_NUMBER, notSetup) as String,
            image = preferences.getString(PHOTO_URL, "") as String,
            isSeller = preferences.getBoolean(SELLER, false),
            isVerified = preferences.getBoolean(VERIFIED, false),
            isAdmin = preferences.getBoolean(ADMIN, false)
        )
    }

    fun saveProfile(profile: UserProfile) {
        preferences.edit()
            .putString(UID, profile.uid)
            .putString(USERNAME, profile.username)
            .putString(ADDRESS, profile.address)
            .putString(PHONE_NUMBER, profile.phone_number)
            .putString(PHOTO_URL, profile.image)
            .putBoolean(SELLER, profile.isSeller)
            .putBoolean(ADMIN, profile.isAdmin)
            .putBoolean(VERIFIED, profile.isVerified)
            .apply()
    }

    fun saveSellerData(application: SellerApplication) {
        preferences.edit()
            .putString(APPLICATION_ID, application.applicationId)
            .putString(PHOTO_ID_URL, application.photoID)
            .putString(ADDRESS, application.address)
            .putString(FULL_NAME, application.fullName)
            .putString(PHONE_NUMBER, application.phoneNumber)
            .putString(PROVINCE, application.province)
            .putString(APPLICATION_STATUS, application.applicationStatus)
            .apply()
    }

    fun getSellerData(): SellerApplication {
        return SellerApplication(
            uid = preferences.getString(UID, notSetup) as String,
            applicationId = preferences.getString(APPLICATION_ID, notSetup) as String,
            fullName = preferences.getString(FULL_NAME, notSetup) as String,
            address = preferences.getString(ADDRESS, notSetup) as String,
            phoneNumber = preferences.getString(PHONE_NUMBER, notSetup) as String,
            photoID = preferences.getString(PHOTO_ID_URL, notSetup) as String,
            province = preferences.getString(PROVINCE, notSetup) as String,
            applicationStatus = preferences.getString(APPLICATION_STATUS, pending) as String
        )
    }

    companion object {
        private const val USER_PREF = "encrypted_user_pref"
    }
}