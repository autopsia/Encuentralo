package com.sectordefectuoso.encuentralo.domain.chat

import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IChatUC {
    suspend fun listServicesByUser(): Flow<ResourceState<List<String>>>
    suspend fun listUsersByService(serviceIds: ArrayList<String>): Flow<ResourceState<ArrayList<Map<String, String>>>>
    suspend fun listData(map: ArrayList<Map<String, String>>): Flow<ResourceState<ArrayList<Map<String, Any>>>>
}