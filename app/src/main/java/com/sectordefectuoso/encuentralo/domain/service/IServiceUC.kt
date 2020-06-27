package com.sectordefectuoso.encuentralo.domain.service

import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState

interface IServiceUC {
    suspend fun getById(uid: String): ResourceState<Service>
    suspend fun create(service: Service): ResourceState<Boolean>
    suspend fun update(service: Service): ResourceState<Boolean>
}