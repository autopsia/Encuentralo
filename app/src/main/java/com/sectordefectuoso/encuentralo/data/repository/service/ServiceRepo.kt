package com.sectordefectuoso.encuentralo.data.repository.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ServiceRepo : IServiceRepo {
    private val auth = FirebaseAuth.getInstance()
    private val userAuth = auth.currentUser
    private val serviceRef = FirebaseFirestore.getInstance().collection("services")

    override suspend fun listByUser(): Flow<ResourceState<List<Service>>> = callbackFlow {
        val uid = userAuth?.uid

        if(uid == null) {
            offer(ResourceState.Failed("Vuelva a iniciar sesión"))
        }
        else{
            val subscription = serviceRef.whereEqualTo("userId", uid).orderBy("createdDate", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
                if (exception != null){
                    offer(ResourceState.Failed(exception.message!!))
                    channel.close()
                }
                else{
                    val services = snapshot!!.toObjects(Service::class.java)
                    offer(ResourceState.Success(services))
                }
            }
            awaitClose {
                subscription.remove()
            }
        }
    }

    override suspend fun getById(uid: String): ResourceState<Service> {
        var resultData = serviceRef.document(uid).get().await()
        val service = resultData.toObject(Service::class.java)
        return ResourceState.Success(service!!)
    }

    override suspend fun create(service: Service): Flow<ResourceState<String>> = flow {
        if(service.userId.isEmpty()){
            service.userId = userAuth!!.uid
        }

        val serviceMap = hashMapOf(
            "title" to service.title,
            "description" to service.description,
            "status" to service.status,
            "createdDate" to service.createdDate,
            "subcategoryId" to service.subcategoryId,
            "userId" to service.userId
        )

        val document = serviceRef.add(serviceMap).await()
        val result = document.id
        emit(ResourceState.Success(result))
    }

    override suspend fun update(service: Service): Flow<ResourceState<String>> = flow {
        val serviceMap = hashMapOf(
            "title" to service.title,
            "description" to service.description,
            "subcategoryId" to service.subcategoryId
        )

        serviceRef.document(service.documentId).set(serviceMap, SetOptions.merge()).await()
        emit(ResourceState.Success(service.documentId))
    }
}