package com.sectordefectuoso.encuentralo.data.repository.service

import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IServiceRepo {
    suspend fun getById(uid: String): ResourceState<Service>
    suspend fun create(service: Service): Flow<ResourceState<Boolean>>
    suspend fun update(service: Service): Flow<ResourceState<Boolean>>
}