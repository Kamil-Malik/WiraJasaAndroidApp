package com.wirajasa.wirajasabisnis.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.wirajasa.wirajasabisnis.usecases.HandleException
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class StorageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val context: Context,
    private val ioDispatcher: CoroutineContext
) : StorageRepository {

    override fun uploadProfileImage(uri: Uri, uid: String): Flow<NetworkResponse<Uri>> =
        flow {
            try {
                val task = firebaseStorage.reference.child("user/$uid.png").putFile(uri).await()
                val photoUri = task.storage.downloadUrl.await()
                emit(NetworkResponse.Success(photoUri))
            } catch (e: Exception) {
                emit(NetworkResponse.GenericException(HandleException().getMessage(e, context)))
            }
        }.onStart { emit(NetworkResponse.Loading) }.flowOn(ioDispatcher)
}