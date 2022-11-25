package com.wirajasa.wirajasabisnis.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.ADDRESS
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.PHONE_NUMBER
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.PHOTO_URL
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.SELLER
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.UID
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.USERNAME

class CryptoPref(mContext: Context) {

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
    private val notSetup = mContext.getString(R.string.not_setup)

    fun getProfile(): UserProfile {
        return UserProfile(
            uid = preferences.getString(UID, notSetup) as String,
            username = preferences.getString(USERNAME, notSetup) as String,
            address = preferences.getString(ADDRESS, notSetup) as String,
            phone_number = preferences.getString(PHONE_NUMBER, notSetup) as String,
            image = preferences.getString(PHOTO_URL, "") as String,
            sellerStatus = preferences.getBoolean(SELLER, false)
        )
    }

    fun saveProfile(profile: UserProfile) {
        preferences.edit()
            .putString(UID, profile.uid)
            .putString(USERNAME, profile.username)
            .putString(ADDRESS, profile.address)
            .putString(PHONE_NUMBER, profile.phone_number)
            .putString(PHOTO_URL, profile.image)
            .putBoolean(SELLER, profile.sellerStatus)
            .apply()
    }

    companion object {
        private const val USER_PREF = "encrypted_user_pref"
    }
}