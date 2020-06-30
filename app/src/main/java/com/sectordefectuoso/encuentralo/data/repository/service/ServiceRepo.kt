package com.sectordefectuoso.encuentralo.data.repository.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ServiceRepo : IServiceRepo {
    private val serviceRef = FirebaseFirestore.getInstance().collection("services")

    override suspend fun getById(uid: String): ResourceState<Service> {
        var resultData = serviceRef.document(uid).get().await()
        val service = resultData.toObject(Service::class.java)
        return ResourceState.Success(service!!)
    }

    override suspend fun create(service: Service): Flow<ResourceState<Boolean>> = flow {
        val serviceMap = hashMapOf(
            "title" to service.title,
            "description" to service.description,
            "status" to service.status,
            "createdDate" to service.createdDate,
            "subcategoryId" to service.subcategoryId,
            "userId" to service.userId
        )

        val document = serviceRef.add(serviceMap).await()
        emit(ResourceState.Success(true))
    }

    override suspend fun update(service: Service): Flow<ResourceState<Boolean>> = flow {
        val serviceMap = hashMapOf(
            "title" to service.title,
            "description" to service.description,
            "createdDate" to service.createdDate,
            "subcategoryId" to service.subcategoryId
        )

        serviceRef.document().set(serviceMap, SetOptions.merge()).await()
        emit(ResourceState.Success(true))
    }
}