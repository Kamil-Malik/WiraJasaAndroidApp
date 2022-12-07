package com.wirajasa.wirajasabisnis.feature_seller.domain.repository_impl

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import com.wirajasa.wirajasabisnis.core.crypto_pref.CryptoPref
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.ADDRESS
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.NAME
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.PHONE_NUMBER
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.PHOTO
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.PRICE
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.PROVINCE
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.SERVICE_ID
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.SLASH
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.UID
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost.Companion.UNIT
import com.wirajasa.wirajasabisnis.core.usecases.HandleException
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_seller.domain.repository.ProductRepository
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump.JPG
import com.wirajasa.wirajasabisnis.core.utility.constant.FirebaseCollection.SERVICE
import com.wirajasa.wirajasabisnis.core.utility.constant.PrefKey.CRYPTO_PREF_UID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductRepositoryImpl @Inject constructor(
    private val context: Context,
    private val storage: StorageReference,
    private val db: FirebaseFirestore,
    private val ioDispatcher: CoroutineContext,
    private val cryptoPref: CryptoPref
) : ProductRepository {

    override fun addProduct(
        uid: String,
        name: String,
        price: Int,
        unit: String,
        address: String,
        province: String,
        phoneNumber: String,
        photo: Uri?
    ): Flow<NetworkResponse<Boolean>> = flow {
        try {
            val id = db.collection(SERVICE).document().id
            val photoReference = storage.child("$SERVICE/$id.jpg")
            photoReference.putFile(photo!!)
                .continueWithTask {
                    photoReference.downloadUrl
                }.continueWithTask { downloadUrlTask ->
                    val servicePost = ServicePost(
                        uid,
                        id,
                        name,
                        price,
                        unit,
                        address,
                        province,
                        phoneNumber,
                        downloadUrlTask.result.toString()
                    )
                    db.collection(SERVICE).document(id).set(servicePost)
                }.await()
            emit(NetworkResponse.Success(data = true))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException(context, e).invoke()))
        }
    }.onStart { emit(NetworkResponse.Loading(null)) }.flowOn(ioDispatcher)

    override fun getLocalProfile(): SellerApplication {
        return cryptoPref.getSellerData()
    }

    override fun getAllProductsAccordingUID(uid: String):
            Flow<NetworkResponse<List<ServicePost>>> = flow {
        try {
            val productReference = db.collection(SERVICE)
                .whereEqualTo(CRYPTO_PREF_UID, uid)
                .get().await().toObjects(ServicePost::class.java)
            emit(NetworkResponse.Success(productReference))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException(context, e).invoke()))
        }
    }.onStart { emit(NetworkResponse.Loading(null)) }.flowOn(ioDispatcher)

    override fun updateProduct(
        uid: String,
        serviceId: String,
        name: String,
        price: Int,
        unit: String,
        address: String,
        province: String,
        phoneNumber: String,
        photo: Uri?,
        photoUrl: String
    ): Flow<NetworkResponse<Boolean>> = flow {
        try {
            if (photo != null){
                storage.child(SERVICE + SLASH + serviceId + JPG )
                    .delete().await()
                val uploadPhoto = storage.child(SERVICE + SLASH + serviceId + JPG)
                uploadPhoto.putFile(photo)
                    .continueWithTask {
                        uploadPhoto.downloadUrl
                    }.continueWithTask { downloadUrlTask ->
                        val servicePost = mapOf(
                            UID to uid,
                            SERVICE_ID to serviceId,
                            NAME to name,
                            PRICE to price,
                            UNIT to unit,
                            ADDRESS to address,
                            PROVINCE to province,
                            PHONE_NUMBER to phoneNumber,
                            PHOTO to downloadUrlTask.result.toString()
                        )
                        db.collection(SERVICE).document(serviceId).set(servicePost, SetOptions.merge())
                    }.await()
            }else{
                val servicePost = mapOf(
                    UID to uid,
                    SERVICE_ID to serviceId,
                    NAME to name,
                    PRICE to price,
                    UNIT to unit,
                    ADDRESS to address,
                    PROVINCE to province,
                    PHONE_NUMBER to phoneNumber,
                    PHOTO to photoUrl
                )
                db.collection(SERVICE).document(serviceId).set(servicePost ,SetOptions.merge()).await()
            }
            emit(NetworkResponse.Success(data = true))
        }catch (e: Exception){
            emit(NetworkResponse.GenericException(HandleException(context, e).invoke()))
        }
    }.onStart { emit(NetworkResponse.Loading(null)) }.flowOn(ioDispatcher)
}