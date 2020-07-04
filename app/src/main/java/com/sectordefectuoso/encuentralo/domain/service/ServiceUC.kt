package com.sectordefectuoso.encuentralo.domain.service

import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.repository.service.IServiceRepo
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

class ServiceUC(private val repo: IServiceRepo) : IServiceUC {
    override suspend fun listByUser(): Flow<ResourceState<List<Service>>> = repo.listByUser()

    override suspend fun getById(uid: String): ResourceState<Service> = repo.getById(uid)

    override suspend fun create(service: Service): Flow<ResourceState<String>> = repo.create(service)

    override suspend fun update(service: Service): Flow<ResourceState<String>> = repo.update(service)

    override suspend fun listByIds(ids: List<String>): Flow<ResourceState<List<Service>>> = repo.listByIds(ids)
}