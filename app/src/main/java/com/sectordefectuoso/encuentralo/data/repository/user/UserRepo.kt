package com.sectordefectuoso.encuentralo.data.repository.user

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepo : IUserRepo {
    private val userRef = FirebaseFirestore.getInstance().collection("users")
    private val auth = FirebaseAuth.getInstance()
    private val userAuth = auth.currentUser

    override suspend fun get(): Flow<ResourceState<User>> = callbackFlow {
        if(userAuth == null){
            offer(ResourceState.Failed("No ha iniciado sesión correctamente"))
        }

        val subscription = userRef.document(userAuth!!.uid).addSnapshotListener { snapshot, exception ->
            if(snapshot?.data == null) {
               offer(ResourceState.Failed("No se encontró su información en la base de datos"))
                cancel("No se encontró su información en la base de datos")
            }
            else{
                val user = snapshot!!.toObject(User::class.java)!!
                offer(ResourceState.Success(user))
            }

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

    override suspend fun getById(uid: String): ResourceState<User> {
        var resultData = userRef.document(uid).get().await()
        val user = resultData.toObject(User::class.java)
        return ResourceState.Success(user!!)
    }

    override suspend fun create(user: User): ResourceState<User> {
        val userMap = hashMapOf(
            "document" to user.document,
            "names" to user.names,
            "lastNames" to user.lastNames,
            "birthdate" to user.birthdate,
            "email" to user.email,
            "phone" to user.phone
        )
        userRef.document(user.documentId).set(userMap).await()
        return getById(user.documentId)
    }

    override suspend fun update(user: User): ResourceState<User> {
        if (userAuth == null){
            throw Exception("El usuario no se encuentra autenticado")
        }

        val userMap = hashMapOf(
            "document" to user.document,
            "names" to user.names,
            "lastNames" to user.lastNames,
            "birthdate" to user.birthdate,
            "email" to user.email,
            "phone" to user.phone
        )
        userRef.document(userAuth.uid).set(userMap, SetOptions.merge()).await()
        return getById(userAuth.uid)
    }

    override suspend fun createAuth(email: String, password: String): ResourceState<String> {
        if(userAuth != null){
            return ResourceState.Success(userAuth.uid)
        }

        var result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("No se pudo registrar la autenticación")
        return ResourceState.Success(uid)
    }

    override suspend fun updateEmail(newEmail: String, prevEmail: String, password: String): ResourceState<Boolean> {
        if (userAuth == null){
            throw Exception("El usuario no se encuentra autenticado")
        }

        val credential = EmailAuthProvider.getCredential(prevEmail, password)
        userAuth.reauthenticate(credential).await()
        userAuth.updateEmail(newEmail).await()
        return ResourceState.Success(true)
    }

    override suspend fun updatePassword(email: String): ResourceState<Boolean> {
        auth.sendPasswordResetEmail(email).await()
        return ResourceState.Success(true)
    }

    override suspend fun logout(): ResourceState<Boolean> {
        if (userAuth == null){
            return ResourceState.Success(true)
        }

        auth.signOut()
        return ResourceState.Success(true)
    }
}