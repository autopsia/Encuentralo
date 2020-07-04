package com.sectordefectuoso.encuentralo.data.repository.chat

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepo: IChatRepo {
    private val auth = FirebaseAuth.getInstance()
    private val userAuth = auth.currentUser
    private val db = FirebaseFirestore.getInstance()

    override suspend fun listServicesByUser(): Flow<ResourceState<List<String>>> = callbackFlow {
        val subscription = db.collectionGroup(userAuth!!.uid).addSnapshotListener { snapshot, exception ->
            if(exception != null) {
                offer(ResourceState.Failed(exception.message!!))
            }
            else{
                var ids = ArrayList<String>()
                for (doc in snapshot!!) {
                    val id = doc.reference.parent.parent!!.id
                    if(ids.indexOf(id) == -1) {
                        ids.add(id)
                    }
                }
                offer(ResourceState.Success(ids))
            }
        }
        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun listUsersByService(serviceId: String): Flow<ResourceState<List<String>>> = callbackFlow {
        val subscription = db.collection("chats").document(serviceId).addSnapshotListener { snapshot, exception ->
            if(exception != null) {
                offer(ResourceState.Failed(exception.message!!))
            }
            else{
                var ids = ArrayList<String>()
                offer(ResourceState.Success(ids))
            }
        }
        awaitClose {
            subscription.remove()
        }
    }
}