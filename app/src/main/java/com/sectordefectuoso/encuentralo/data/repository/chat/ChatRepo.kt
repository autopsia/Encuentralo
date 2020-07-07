package com.sectordefectuoso.encuentralo.data.repository.chat

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

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

    override suspend fun listUsersByService(serviceIds: ArrayList<String>): Flow<ResourceState<ArrayList<Map<String, String>>>> = flow {
        val list = ArrayList<Map<String, String>>()
        for(serviceId in serviceIds) {
            val document = db.collection("chats").document(serviceId).get().await()
            val userIds = document.get("chatList") as ArrayList<String>
            for(userId in userIds) {
                val map = hashMapOf("serviceId" to serviceId, "userId" to userId)
                list.add(map)
            }
        }
        emit(ResourceState.Success(list))
    }

    override suspend fun listData(map: ArrayList<Map<String, String>>): Flow<ResourceState<ArrayList<Map<String, Any>>>> = flow {
        val list = ArrayList<Map<String, Any>>()
        for(data in map) {
            val serviceId = data["serviceId"] ?: error("")
            val serviceDoc = db.collection("services").document(serviceId).get().await()
            val service = serviceDoc.toObject(Service::class.java)

            val userId = data["userId"] ?: error("")
            val userDoc = db.collection("users").document(userId).get().await()
            val user = userDoc.toObject(User::class.java)

            val result = hashMapOf("service" to service!!, "user" to user!!)
            list.add(result)
        }
        emit(ResourceState.Success(list))
    }
}