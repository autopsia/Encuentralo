package com.sectordefectuoso.encuentralo.data.repository.service

import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IServiceRepo {
    suspend fun listByUser(): Flow<ResourceState<List<Service>>>
    suspend fun getById(uid: String): ResourceState<Service>
    suspend fun create(service: Service): Flow<ResourceState<String>>
    suspend fun update(service: Service): Flow<ResourceState<String>>
    suspend fun listByIds(ids: List<String>): Flow<ResourceState<List<Service>>>
}