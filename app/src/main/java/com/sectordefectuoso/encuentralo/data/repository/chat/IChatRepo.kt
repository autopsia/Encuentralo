package com.sectordefectuoso.encuentralo.data.repository.chat

import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IChatRepo {
    suspend fun listServicesByUser(): Flow<ResourceState<List<String>>>
    suspend fun listUsersByService(serviceIds: ArrayList<String>): Flow<ResourceState<ArrayList<Map<String, String>>>>
    suspend fun listData(map: ArrayList<Map<String, String>>): Flow<ResourceState<ArrayList<Map<String, Any>>>>
}