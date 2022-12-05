package com.wirajasa.wirajasabisnis.feature_auth.domain.repository_impl


import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.crypto_pref.CryptoPref
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.domain.model.UserProfile
import com.wirajasa.wirajasabisnis.feature_auth.domain.repository.AuthRepository
import com.wirajasa.wirajasabisnis.core.usecases.HandleException
import com.wirajasa.wirajasabisnis.utility.Constant.COLLECTION_USER
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.CRYPTO_PREF_SELLER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineContext,
    private val cryptoPref: CryptoPref
) : AuthRepository {

    override fun signInWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<UserProfile>> = flow {
        try {
            auth.signInWithEmailAndPassword(email, password).await()

            emit(NetworkResponse.Loading(context.getString(R.string.tv_getting_profile)))
            val userProfile =
                db.collection(COLLECTION_USER).document(auth.currentUser?.uid as String)
                    .get().await().toObject(UserProfile::class.java) as UserProfile

            saveProfile(userProfile)

            if(userProfile.isSeller) {
                emit(NetworkResponse.Loading("Getting Seller Data"))
                val sellerData = db.collection(CRYPTO_PREF_SELLER).whereEqualTo(UserProfile.USERID, userProfile.uid).get()
                    .await().toObjects(SellerApplication::class.java)
                cryptoPref.saveSellerData(sellerData[0])
            }
            emit(NetworkResponse.Success(userProfile))
        } catch (e: Exception) {
            emit(
                NetworkResponse.GenericException(
                    HandleException(context, e).invoke()
                )
            )
        }
    }.onStart { emit(NetworkResponse.Loading(context.getString(R.string.tv_signing_in))) }
        .flowOn(ioDispatcher)

    override fun signUpWithEmailAndPassword(
        email: String, password: String
    ): Flow<NetworkResponse<UserProfile>> = flow {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()

            emit(NetworkResponse.Loading(context.getString(R.string.tv_uploading_profile)))
            val defaultProfile = getDefaultProfile()
            db.collection(COLLECTION_USER).document((getCurrentUser() as FirebaseUser).uid)
                .set(defaultProfile)
                .continueWith {
                    val createdAndUpdated: HashMap<String, Timestamp?> = hashMapOf(
                        "created_at" to Timestamp.now(),
                        "updated_at" to null
                    )
                    db.collection(COLLECTION_USER).document((getCurrentUser() as FirebaseUser).uid)
                        .set(createdAndUpdated, SetOptions.merge())
                }.await()

            saveProfile(defaultProfile)
            emit(NetworkResponse.Success(defaultProfile))
        } catch (e: Exception) {
            emit(
                NetworkResponse.GenericException(
                    HandleException(context, e).invoke()
                )
            )
        }
    }.onStart { emit(NetworkResponse.Loading(context.getString(R.string.tv_signing_up))) }
        .flowOn(ioDispatcher)

    override fun resetPasswordWithEmail(
        email: String
    ): Flow<NetworkResponse<Boolean>> = flow {
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(NetworkResponse.Success(true))
        } catch (e: Exception) {
            emit(
                NetworkResponse.GenericException(
                    HandleException(context, e).invoke()
                )
            )
        }
    }.onStart { emit(NetworkResponse.Loading(null)) }
        .flowOn(ioDispatcher)

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    private fun getDefaultProfile(): UserProfile {
        val notSetup = context.getString(R.string.tv_not_setup)
        return UserProfile(
            uid = (auth.currentUser as FirebaseUser).uid,
            username = "Guest${UUID.randomUUID()}",
            address = notSetup,
            phone_number = notSetup,
            isSeller = false,
        )
    }

    private fun saveProfile(userProfile: UserProfile) {
        cryptoPref.saveProfile(userProfile)
    }
}