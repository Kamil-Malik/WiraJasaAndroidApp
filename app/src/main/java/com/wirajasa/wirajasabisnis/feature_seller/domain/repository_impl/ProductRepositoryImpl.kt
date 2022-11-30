package com.wirajasa.wirajasabisnis.feature_seller.domain.repository_impl

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.wirajasa.wirajasabisnis.core.crypto_pref.CryptoPref
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.domain.model.ServicePost
import com.wirajasa.wirajasabisnis.core.usecases.HandleException
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_seller.domain.repository.ProductRepository
import com.wirajasa.wirajasabisnis.utility.constant.FirebaseCollection.SERVICE
import com.wirajasa.wirajasabisnis.utility.constant.PrefKey.UID
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
    private val firestoreDb: FirebaseFirestore,
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
            val id = firestoreDb.collection(SERVICE).document().id
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
                    firestoreDb.collection(SERVICE).document(id).set(servicePost)
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
            Flow<NetworkResponse<MutableList<ServicePost>>> = flow {
        try {
            val productList: MutableList<ServicePost> = mutableListOf()
            val productReference = firestoreDb.collection(SERVICE)
                .whereEqualTo(UID, uid)
            productReference.addSnapshotListener { snapshot, error ->
                if (error == null) {
                    val product = snapshot?.toObjects(ServicePost::class.java)
                    productList.addAll(product!!)
                }
            }
            emit(NetworkResponse.Success(productList))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException(context, e).invoke()))
        }
    }.onStart { emit(NetworkResponse.Loading(null)) }.flowOn(ioDispatcher)
}