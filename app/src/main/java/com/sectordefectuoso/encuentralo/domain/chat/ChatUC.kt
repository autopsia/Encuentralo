package com.sectordefectuoso.encuentralo.domain.chat

import com.sectordefectuoso.encuentralo.data.repository.chat.IChatRepo
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

class ChatUC(private val repo: IChatRepo): IChatUC {
    override suspend fun listServicesByUser(): Flow<ResourceState<List<String>>> = repo.listServicesByUser()
    override suspend fun listUsersByService(serviceId: String): Flow<ResourceState<List<String>>> = repo.listUsersByService(serviceId)
}