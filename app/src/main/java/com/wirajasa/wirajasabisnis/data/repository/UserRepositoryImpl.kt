package com.wirajasa.wirajasabisnis.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.edit
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.data.model.UserProfile
import com.wirajasa.wirajasabisnis.usecases.HandleException
import com.wirajasa.wirajasabisnis.utility.Constant
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.utility.constant.FirebaseCollection.USER
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.ADDRESS
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.CREATED_AT
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.PHONE_NUMBER
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.PHOTO_URL
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.SELLER
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.UID
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.USERNAME
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.USER_PREF
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UserRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val fireStore: FirebaseFirestore,
    private val authRepository: AuthRepository,
    private val context: Context,
    private val ioDispatcher: CoroutineContext
) : UserRepository {

    override fun uploadProfileImage(uri: Uri, uid: String): Flow<NetworkResponse<Uri>> =
        flow {
            try {
                val task = storage.reference.child("$USER/$uid.png").putFile(uri).await()
                emit(NetworkResponse.Success(task.storage.downloadUrl.await()))
            } catch (e: Exception) {
                emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
            }
        }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun registerDefaultProfile(): Flow<NetworkResponse<UserProfile>> =
        flow {
            try {
                val currentUser = authRepository.getCurrentUser() as FirebaseUser
                val notSetup = context.getString(R.string.not_setup)
                val profile = UserProfile(
                    uid = currentUser.uid,
                    username = "Guest${UUID.randomUUID()}",
                    address = notSetup,
                    phone_number = notSetup,
                    image = "",
                    seller = false,
                    createdAt = Timestamp.now().toDate().toString()
                )
                fireStore.collection(Constant.COLLECTION_USER).document(currentUser.uid)
                    .set(profile).await()
                saveProfile(profile)
                emit(NetworkResponse.Success(profile))
            } catch (e: Exception) {
                emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
            }
        }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun updateProfile(userProfile: UserProfile): Flow<NetworkResponse<Boolean>> =
        flow {
            try {
                saveProfile(userProfile)
                fireStore.collection(USER).document(userProfile.uid)
                    .set(userProfile, SetOptions.merge()).await()
                emit(NetworkResponse.Success(true))
            } catch (e: Exception) {
                emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
            }
        }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun getUserProfile(): Flow<NetworkResponse<UserProfile>> =
        flow {
            try {
                val uid = authRepository.getCurrentUser()?.uid as String
                val result =
                    fireStore.collection(USER).document(uid)
                        .get().await()
                        .toObject(UserProfile::class.java) as UserProfile
                saveProfile(result)
                emit(NetworkResponse.Success(result))
            } catch (e: Exception) {
                emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
            }
        }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)

    override fun getLocalProfile(): UserProfile {
        val sharedPref = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        val notSetup = context.getString(R.string.not_setup)
        return UserProfile(
            uid = sharedPref.getString(UID, notSetup) as String,
            username = sharedPref.getString(USERNAME, notSetup) as String,
            address = sharedPref.getString(ADDRESS, notSetup) as String,
            phone_number = sharedPref.getString(PHONE_NUMBER, notSetup) as String,
            image = sharedPref.getString(PHOTO_URL, "") as String,
            seller = sharedPref.getBoolean(SELLER, false),
            createdAt = sharedPref.getString(CREATED_AT, "") as String
        )
    }

    private fun saveProfile(profile: UserProfile) {
        context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).edit {
            putString(UID, profile.uid)
            putString(USERNAME, profile.username)
            putString(ADDRESS, profile.address)
            putString(PHONE_NUMBER, profile.phone_number)
            putString(PHOTO_URL, profile.image)
            putBoolean(SELLER, profile.seller)
            putString(CREATED_AT, profile.createdAt)
            apply()
        }
    }
}