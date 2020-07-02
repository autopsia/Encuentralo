package com.sectordefectuoso.encuentralo.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PostListRepository @Inject constructor() : IPostListRepository {
    val serviceRef = FirebaseFirestore
        .getInstance()
        .collection("services")

    val userRef = FirebaseFirestore
        .getInstance()
        .collection("users")

    override suspend fun getServiceListBySubCategoryId(subCategoryId: String): Flow<ResourceState<List<Service>>> = callbackFlow {
        val subscription = serviceRef.whereEqualTo("subcategoryId", subCategoryId).addSnapshotListener { snapshot, exception ->
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

    suspend fun getServiceById(serviceId : String) : Flow<ResourceState<Service?>> = callbackFlow {
        val subscription = serviceRef.document(serviceId).addSnapshotListener { snapshot, exception ->
            offer(
                ResourceState.Success(snapshot!!.toObject(Service::class.java))
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

    suspend fun getUserById(userId : String) : Flow<ResourceState<User?>> = callbackFlow {
        val subscription = userRef.document(userId).addSnapshotListener { snapshot, exception ->
            offer(
                ResourceState.Success(snapshot!!.toObject(User::class.java))
            )

            exception.let {
                offer(ResourceState.Failed(it?.message.toString()))
                cancel(it?.message.toString())
            }
        }
        awaitClose {
            subscription.remove()
            cancel()
        }
    }
}