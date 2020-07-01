package com.sectordefectuoso.encuentralo.data.repository.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRepo : IUserRepo {
    private val userRef = FirebaseFirestore.getInstance().collection("users")
    private val auth = FirebaseAuth.getInstance()
    private val userAuth = auth.currentUser

    //Base de datos
    @ExperimentalCoroutinesApi
    override suspend fun get(): Flow<ResourceState<User>> = callbackFlow {
        val subscription =
            userRef.document(userAuth!!.uid).addSnapshotListener { snapshot, exception ->
                if (snapshot!!.exists()) {
                    val user = snapshot!!.toObject(User::class.java)!!
                    offer(ResourceState.Success(user))
                } else {
                    channel.close()
                }
            }
        awaitClose {
            subscription.remove()
        }
    }

    override suspend fun getById(uid: String): ResourceState<User> {
        var resultData = userRef.document(uid).get().await()
        val user = resultData.toObject(User::class.java)
        return ResourceState.Success(user!!)
    }

    override suspend fun create(user: User): Flow<ResourceState<Boolean>> = flow {
        if (!user.documentId.isEmpty()) {
            val userMap = hashMapOf(
                "document" to user.document,
                "names" to user.names,
                "lastNames" to user.lastNames,
                "birthdate" to user.birthdate,
                "email" to user.email,
                "phone" to user.phone,
                "dateCreated" to FieldValue.serverTimestamp(),
                "lastLogin" to FieldValue.serverTimestamp()
            )

            userRef.document(user.documentId).set(userMap).await()
            emit(ResourceState.Success(true))
        } else {
            throw Exception("No se pudo registrar en la base de datos")
        }
    }

    override suspend fun update(user: User): Flow<ResourceState<Boolean>> = flow {
        if (userAuth == null) {
            throw Exception("El usuario no se encuentra autenticado")
        } else {
            val userMap = hashMapOf(
                "document" to user.document,
                "names" to user.names,
                "lastNames" to user.lastNames,
                "birthdate" to user.birthdate,
                "phone" to user.phone
            )
            userRef.document(userAuth.uid).set(userMap, SetOptions.merge()).await()
            emit(ResourceState.Success(true))
        }
    }

    override suspend fun updateLastLogin(uid: String): Flow<ResourceState<Boolean>> = flow {
        var result = false
        if (userAuth != null) {
            val userMap = hashMapOf(
                "lastLogin" to FieldValue.serverTimestamp()
            )
            userRef.document(uid).set(userMap, SetOptions.merge()).await()
            result = true
        }

        emit(ResourceState.Success(result))
    }

    //Autenticación
    override suspend fun createAuth(email: String, password: String): ResourceState<String> {
        if (userAuth != null) {
            return ResourceState.Success(userAuth.uid)
        }

        var result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("No se pudo registrar la autenticación")
        return ResourceState.Success(uid)
    }

    override suspend fun updatePassword(email: String): ResourceState<Boolean> {
        auth.sendPasswordResetEmail(email).await()
        return ResourceState.Success(true)
    }

    override suspend fun logout(): ResourceState<Boolean> {
        if (userAuth == null) {
            return ResourceState.Success(true)
        }

        auth.signOut()
        return ResourceState.Success(true)
    }
}