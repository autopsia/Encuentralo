package com.sectordefectuoso.encuentralo.domain.chat

import com.sectordefectuoso.encuentralo.data.repository.chat.IChatRepo
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

class ChatUC(private val repo: IChatRepo): IChatUC {
    override suspend fun listServicesByUser(): Flow<ResourceState<List<String>>> = repo.listServicesByUser()
    override suspend fun listUsersByService(serviceIds: ArrayList<String>): Flow<ResourceState<ArrayList<Map<String, String>>>> = repo.listUsersByService(serviceIds)
    override suspend fun listData(map: ArrayList<Map<String, String>>): Flow<ResourceState<ArrayList<Map<String, Any>>>> = repo.listData(map)
}