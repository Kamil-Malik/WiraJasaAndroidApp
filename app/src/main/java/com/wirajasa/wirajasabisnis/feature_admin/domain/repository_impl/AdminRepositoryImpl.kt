package com.wirajasa.wirajasabisnis.feature_admin.domain.repository_impl

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.wirajasa.wirajasabisnis.R
import com.wirajasa.wirajasabisnis.core.domain.model.SellerApplication
import com.wirajasa.wirajasabisnis.core.usecases.HandleException
import com.wirajasa.wirajasabisnis.core.utility.NetworkResponse
import com.wirajasa.wirajasabisnis.feature_admin.domain.repository.AdminRepository
import com.wirajasa.wirajasabisnis.core.utility.constant.ApplicationStatus.APPROVED
import com.wirajasa.wirajasabisnis.core.utility.constant.ApplicationStatus.REJECTED
import com.wirajasa.wirajasabisnis.core.utility.constant.Dump.VERIFIED
import com.wirajasa.wirajasabisnis.core.utility.constant.FirebaseCollection.SELLER
import com.wirajasa.wirajasabisnis.core.utility.constant.FirebaseCollection.USER
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val mContext: Context,
    private val ioDispatcher: CoroutineDispatcher
) : AdminRepository {

    override fun getApplicationForm(): Flow<NetworkResponse<List<SellerApplication>>> = flow {
        try {
            val data = db.collection(SELLER).get().await().toObjects(SellerApplication::class.java)
                .sortedBy { it.applicationStatus }
            emit(NetworkResponse.Success(data))
        } catch (e: Exception) {
            emit(NetworkResponse.GenericException(HandleException(mContext, e).invoke()))
        }
    }.onStart { emit(NetworkResponse.Loading(mContext.getString(R.string.loading_status_fetching_data))) }.flowOn(ioDispatcher)

    override fun updateApplicationForm(form: SellerApplication): Flow<NetworkResponse<Boolean>> =
        flow {
            try {
                db.collection(SELLER).document(form.applicationId).set(form, SetOptions.merge())
                    .await()

                if (form.applicationStatus == APPROVED) {
                    emit(NetworkResponse.Loading(mContext.getString(R.string.loading_status_updating_seller_status)))
                    val isApproved = hashMapOf(
                        VERIFIED to true
                    )
                    db.collection(USER).document(form.uid)
                        .set(isApproved, SetOptions.merge())
                        .await()
                } else if (form.applicationStatus == REJECTED) {
                    emit(NetworkResponse.Loading(mContext.getString(R.string.loading_status_updating_seller_status)))
                    val isApproved = hashMapOf(
                        VERIFIED to false
                    )
                    db.collection(USER).document(form.uid)
                        .set(isApproved, SetOptions.merge())
                        .await()
                }

                emit(NetworkResponse.Success(true))
            } catch (e: Exception) {
                emit(
                    NetworkResponse.GenericException(HandleException(mContext, e).invoke())
                )
            }
        }.onStart { emit(NetworkResponse.Loading(mContext.getString(R.string.loading_status_updating_form_status))) }
            .flowOn(ioDispatcher)
}