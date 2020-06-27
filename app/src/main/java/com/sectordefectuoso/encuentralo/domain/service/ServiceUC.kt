package com.sectordefectuoso.encuentralo.domain.service

import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.repository.service.IServiceRepo
import com.sectordefectuoso.encuentralo.utils.ResourceState

class ServiceUC(private val repo: IServiceRepo) : IServiceUC {
    override suspend fun getById(uid: String): ResourceState<Service> = repo.getById(uid)

    override suspend fun create(service: Service): ResourceState<Boolean> = repo.create(service)

    override suspend fun update(service: Service): ResourceState<Boolean> = repo.update(service)
}