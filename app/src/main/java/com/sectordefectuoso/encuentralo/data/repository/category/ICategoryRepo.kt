package com.sectordefectuoso.encuentralo.data.repository.category

import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface ICategoryRepo {
    suspend fun getAll(): Flow<ResourceState<List<Category>>>
    suspend fun getSubcategoriesByCategory(idCategory: String): Flow<ResourceState<List<SubCategory>>>
    suspend fun getCategoryBySubcategory(idSubCategory: String): Flow<ResourceState<String>>
}