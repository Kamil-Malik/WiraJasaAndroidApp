package com.wirajasa.wirajasabisnis.core.domain.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.crypto_pref.CryptoPref
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.usecases.HandleException
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.utility.constant.FirebaseCollection.USER
import com.wirajasa.wirajasabisnis.utility.constant.FirebaseCollection.SELLER
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SellerRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val ioDispatcher: CoroutineDispatcher,
    private val context: Context,
    private val cryptoPref: CryptoPref
) : SellerRepository {

    override fun submitApplication(form: SellerApplication): Flow<NetworkResponse<Boolean>> = flow {
        try {
            val uri = storage.reference.child("$SELLER/${form.uid}.png")
                .putFile(Uri.parse(form.photoID)).await().storage.downloadUrl.await()
            form.photoID = uri.toString()
            emit(NetworkResponse.Loading(context.getString(R.string.loading_status_uploading_application_form)))

            val id = db.collection(SELLER).document().id
            form.applicationId = id
            db.collection(SELLER).document(id)
                .set(form, SetOptions.merge()).await()

            emit(NetworkResponse.Loading(context.getString(R.string.loading_status_updating_profile_data)))
            val user = cryptoPref.getProfile()
            user.isSeller = true
            cryptoPref.saveProfile(user)
            db.collection(USER).document(form.uid).set(user, SetOptions.merge()).continueWith {
                val update = hashMapOf(
                    "updated_at" to Timestamp.now()
                )
                db.collection(USER).document(form.uid).set(update, SetOptions.merge())
            }.await()
            cryptoPref.saveSellerData(form)
            emit(NetworkResponse.Success(true))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException(context, e).invoke()))
        }
    }.onStart { emit(NetworkResponse.Loading(context.getString(R.string.loading_status_uploading_image))) }
        .flowOn(ioDispatcher)

    override fun getSellerData(): SellerApplication {
        return cryptoPref.getSellerData()
    }
}