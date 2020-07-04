package com.sectordefectuoso.encuentralo.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sectordefectuoso.encuentralo.data.model.ChatList
import com.sectordefectuoso.encuentralo.data.model.ChatMessage
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatListRepository @Inject constructor() {

    val serviceRef = FirebaseFirestore
        .getInstance()
        .collection("services")
    val chatRef = FirebaseFirestore
        .getInstance()
        .collection("chats")
    suspend fun getAllServiceChatReferences(serviceId : String ) : Flow<ResourceState<ChatList?>> = callbackFlow {
        val subscription = chatRef.document(serviceId).addSnapshotListener { snapshot, exception ->
            offer(
                ResourceState.Success(snapshot!!.toObject(ChatList::class.java))
            )
            exception?.let {
                offer(ResourceState.Failed(it.message.toString()))
            }
        }
        awaitClose {
            subscription.remove()
            cancel()
        }
    }

    suspend fun getServiceChats(authorId: String): Flow<ResourceState<List<Service>>> = callbackFlow {
        val subscription = serviceRef.whereEqualTo("authorId", authorId).orderBy("createdDate", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
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

    suspend fun getServiceListByAuthorId(authorId: String): Flow<ResourceState<List<Service>>> = callbackFlow {
        val subscription = serviceRef.whereEqualTo("authorId", authorId).orderBy("createdDate", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
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