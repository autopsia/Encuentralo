package com.sectordefectuoso.encuentralo.domain

import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IUseCase {
    suspend fun getCategories(): Flow<ResourceState<List<Category>>>
    suspend fun getSubCategoriesFromCategory(id:String): Flow<ResourceState<List<SubCategory>>>
}