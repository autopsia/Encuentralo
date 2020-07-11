package com.sectordefectuoso.encuentralo.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageException
import com.sectordefectuoso.encuentralo.data.model.ChatMessage
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepository @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()
    private val userRef = firestore.collection("users")
    private val userAuth = FirebaseAuth.getInstance().currentUser
    private val chatRef = firestore.collection("chats")

    suspend fun getChat(serviceId : String, userId : String) : Flow<ResourceState<List<ChatMessage>>> = callbackFlow {
        val subscription = chatRef.document(serviceId).collection(userId).orderBy("dateCreated", Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->
            offer(
                ResourceState.Success(snapshot!!.toObjects(ChatMessage::class.java))
            )
            exception.let {
                offer(ResourceState.Failed(it?.message.toString()))
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

     fun sendMessage(serviceId: String,authorId:String, chatMessage: ChatMessage) : Flow<ResourceState<Boolean>> = flow {
        Log.i("ChatRepository",serviceId+authorId+chatMessage)
        try {
            if (chatMessage.message.isNotEmpty()) {

                val userMap = hashMapOf(
                    "message" to chatMessage.message,
                    "dateCreated" to FieldValue.serverTimestamp(),
                    "author" to chatMessage.author,
                    "type" to chatMessage.type
                )

                chatRef.document(serviceId).collection(authorId).document().set(userMap).await()
                chatRef.document(serviceId).update("chatList", FieldValue.arrayUnion(authorId))
                emit(ResourceState.Success(true))
            } else {
                throw Exception("No se pudo registrar en la base de datos")
            }
            }catch (e : StorageException){
            throw e
        }

    }

    fun setAgreement(serviceId: String, chatMessage: ChatMessage) : Flow<ResourceState<Boolean>> = flow {

        try {
            if (chatMessage.message.isNotEmpty()) {
                Log.i("setAgreement!!", serviceId)
                Log.i("setAgreement", chatMessage.toString())
                chatRef.document(serviceId).collection(chatMessage.author).document(chatMessage.documentId).update("message", chatMessage.message)
                emit(ResourceState.Success(true))
            } else {
                throw Exception("No se pudo registrar en la base de datos")
            }
        }catch (e : StorageException){
            throw e
        }

    }
}