package com.sectordefectuoso.encuentralo.data.repository

import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository{
    suspend fun getAll(): Flow<ResourceState<List<Category>>>?
}