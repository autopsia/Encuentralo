package com.sectordefectuoso.encuentralo.data.repository.chat

import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IChatRepo {
    suspend fun listServicesByUser(): Flow<ResourceState<List<String>>>
    suspend fun listUsersByService(serviceId: String): Flow<ResourceState<List<String>>>
}