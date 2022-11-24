package com.wirajasa.wirajasabisnis.data.repository

import android.net.Uri
import com.wirajasa.wirajasabisnis.utility.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    fun uploadProfileImage(uri: Uri, uid: String): Flow<NetworkResponse<Uri>>
}