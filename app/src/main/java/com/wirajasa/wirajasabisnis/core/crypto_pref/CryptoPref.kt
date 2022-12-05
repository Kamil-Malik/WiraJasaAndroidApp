package com.wirajasa.wirajasabisnis.core.crypto_pref

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_ADDRESS
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_ADMIN
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_APPLICATION_ID
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_APPLICATION_STATUS
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_FULL_NAME
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_PHONE_NUMBER
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_PHOTO_ID_URL
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_PHOTO_URL
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_PROVINCE
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_SELLER
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_UID
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_USERNAME
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_USER_PREF
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_VERIFIED

class CryptoPref(mContext: Context) {

    private val notSetup = mContext.getString(R.string.tv_not_setup)
    private val pending = mContext.getString(R.string.tv_pending)
    private val masterKey: MasterKey = MasterKey.Builder(mContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .setUserAuthenticationRequired(false)
        .build()
    private val preferences = EncryptedSharedPreferences.create(
        mContext,
        CRYPTO_PREF_USER_PREF,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getProfile(): UserProfile {
        return UserProfile(
            uid = preferences.getString(CRYPTO_PREF_UID, notSetup) as String,
            username = preferences.getString(CRYPTO_PREF_USERNAME, notSetup) as String,
            address = preferences.getString(CRYPTO_PREF_ADDRESS, notSetup) as String,
            phone_number = preferences.getString(CRYPTO_PREF_PHONE_NUMBER, notSetup) as String,
            image = preferences.getString(CRYPTO_PREF_PHOTO_URL, "") as String,
            isSeller = preferences.getBoolean(CRYPTO_PREF_SELLER, false),
            isVerified = preferences.getBoolean(CRYPTO_PREF_VERIFIED, false),
            isAdmin = preferences.getBoolean(CRYPTO_PREF_ADMIN, false)
        )
    }

    fun saveProfile(profile: UserProfile) {
        preferences.edit()
            .putString(CRYPTO_PREF_UID, profile.uid)
            .putString(CRYPTO_PREF_USERNAME, profile.username)
            .putString(CRYPTO_PREF_ADDRESS, profile.address)
            .putString(CRYPTO_PREF_PHONE_NUMBER, profile.phone_number)
            .putString(CRYPTO_PREF_PHOTO_URL, profile.image)
            .putBoolean(CRYPTO_PREF_SELLER, profile.isSeller)
            .putBoolean(CRYPTO_PREF_ADMIN, profile.isAdmin)
            .putBoolean(CRYPTO_PREF_VERIFIED, profile.isVerified)
            .apply()
    }

    fun saveSellerData(application: SellerApplication) {
        preferences.edit()
            .putString(CRYPTO_PREF_APPLICATION_ID, application.applicationId)
            .putString(CRYPTO_PREF_PHOTO_ID_URL, application.photoID)
            .putString(CRYPTO_PREF_ADDRESS, application.address)
            .putString(CRYPTO_PREF_FULL_NAME, application.fullName)
            .putString(CRYPTO_PREF_PHONE_NUMBER, application.phoneNumber)
            .putString(CRYPTO_PREF_PROVINCE, application.province)
            .putString(CRYPTO_PREF_APPLICATION_STATUS, application.applicationStatus)
            .apply()
    }

    fun getSellerData(): SellerApplication {
        return SellerApplication(
            uid = preferences.getString(CRYPTO_PREF_UID, notSetup) as String,
            applicationId = preferences.getString(CRYPTO_PREF_APPLICATION_ID, notSetup) as String,
            fullName = preferences.getString(CRYPTO_PREF_FULL_NAME, notSetup) as String,
            address = preferences.getString(CRYPTO_PREF_ADDRESS, notSetup) as String,
            phoneNumber = preferences.getString(CRYPTO_PREF_PHONE_NUMBER, notSetup) as String,
            photoID = preferences.getString(CRYPTO_PREF_PHOTO_ID_URL, notSetup) as String,
            province = preferences.getString(CRYPTO_PREF_PROVINCE, notSetup) as String,
            applicationStatus = preferences.getString(CRYPTO_PREF_APPLICATION_STATUS, pending) as String
        )
    }
}