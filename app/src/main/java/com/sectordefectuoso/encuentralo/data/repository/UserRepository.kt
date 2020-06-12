package com.sectordefectuoso.encuentralo.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class UserRepository {
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection("users")

    suspend fun getUserByUid(uid: String): Flow<ResourceState<User?>> = callbackFlow {
        val subscription = userRef.document(uid).addSnapshotListener { snapshot, exception ->
            offer(ResourceState.Success(snapshot!!.toObject(User::class.java)))
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