package com.sectordefectuoso.encuentralo.domain.category

import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface ICategoryUC {
    suspend fun getAll(): Flow<ResourceState<List<Category>>>
    suspend fun getSubcategoriesByCategory(idCategory: String): Flow<ResourceState<List<SubCategory>>>
}