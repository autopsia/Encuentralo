package com.sectordefectuoso.encuentralo.domain.chat

import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IChatUC {
    suspend fun listServicesByUser(): Flow<ResourceState<List<String>>>
    suspend fun listUsersByService(serviceId: String): Flow<ResourceState<List<String>>>
}