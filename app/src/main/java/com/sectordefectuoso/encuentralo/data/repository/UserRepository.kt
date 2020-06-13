package com.sectordefectuoso.encuentralo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class UserRepository {
    private val userRef = FirebaseFirestore.getInstance().collection("users")
    private val userAuth = FirebaseAuth.getInstance().currentUser

    suspend fun getUserByUid(): Flow<ResourceState<User?>> = callbackFlow {
        val uid = userAuth!!.uid
        val subscription = userRef.document(uid).addSnapshotListener { snapshot, exception ->
            offer(ResourceState.Success(snapshot!!.toObject(User::class.java)))
            exception?.let {
                offer(ResourceState.Failed(it.message.toString()))
            }
        }
        awaitClose {
            subscription.remove()
            cancel()
        }
    }

    fun updateEmail(email: String): Flow<ResourceState<Boolean>> = callbackFlow {
        userAuth?.updateEmail(email)?.addOnCompleteListener { task ->
            if(task.isSuccessful){
                offer(ResourceState.Success(true))
            } else{
                offer(ResourceState.Failed("No se pudo actualizar el correo electrónico"))
            }
        }
        awaitClose { cancel() }
    }

    fun updateInformation(user: User): Flow<ResourceState<Boolean>> = callbackFlow {
        val userMap = hashMapOf(
            "document" to user.document,
            "names" to user.names,
            "lastNames" to user.lastNames,
            "birthdate" to user.birthdate,
            "phone" to user.phone,
            "email" to user.email
        )

        userRef.document(userAuth!!.uid).set(userMap, SetOptions.merge()).addOnCompleteListener { task ->
            if(task.isSuccessful){
                offer(ResourceState.Success(true))
            }
            else{
                offer(ResourceState.Failed("No se pudo actualizar la información"))
                cancel("No se pudo actualizar la información")
            }
        }
        awaitClose { cancel() }
    }
}