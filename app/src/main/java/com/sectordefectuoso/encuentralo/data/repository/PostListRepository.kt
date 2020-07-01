package com.sectordefectuoso.encuentralo.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PostListRepository @Inject constructor() : IPostListRepository {
    val serviceRef = FirebaseFirestore
        .getInstance()
        .collection("service")

    override suspend fun getServiceListBySubCategoryId(subCategoryId: String): Flow<ResourceState<List<Service>>> = callbackFlow {
        val subscription = serviceRef.whereArrayContains("subCategoryId", subCategoryId).addSnapshotListener { snapshot, exception ->
            offer(
                ResourceState.Success(snapshot!!.toObjects(Service::class.java))
            )

            exception?.let {
                offer(ResourceState.Failed(it.message.toString()))
                cancel(it.message.toString())
            }
        }
        awaitClose {
            subscription.remove()
            cancel()
        }
    }
}