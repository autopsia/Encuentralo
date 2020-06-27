package com.sectordefectuoso.encuentralo.data.repository.service

import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState

interface IServiceRepo {
    suspend fun getById(uid: String): ResourceState<Service>
    suspend fun create(service: Service): ResourceState<Boolean>
    suspend fun update(service: Service): ResourceState<Boolean>
}