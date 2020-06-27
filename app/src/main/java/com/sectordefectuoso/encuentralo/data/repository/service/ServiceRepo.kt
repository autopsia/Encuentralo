package com.sectordefectuoso.encuentralo.data.repository.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.tasks.await

class ServiceRepo : IServiceRepo {
    private val serviceRef = FirebaseFirestore.getInstance().collection("services")

    override suspend fun getById(uid: String): ResourceState<Service> {
        var resultData = serviceRef.document(uid).get().await()
        val service = resultData.toObject(Service::class.java)
        return ResourceState.Success(service!!)
    }

    override suspend fun create(service: Service): ResourceState<Boolean> {
        val userMap = Gson().toJson(service)
        val document = serviceRef.add(userMap).await()
        return ResourceState.Success(true)
    }

    override suspend fun update(service: Service): ResourceState<Boolean> {
        val userMap = Gson().toJson(service)
        serviceRef.document().set(userMap, SetOptions.merge()).await()
        return ResourceState.Success(true)
    }
}