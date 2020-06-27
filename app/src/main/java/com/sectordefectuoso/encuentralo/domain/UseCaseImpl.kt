package com.sectordefectuoso.encuentralo.domain

import androidx.lifecycle.LiveData
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

class UseCaseImpl (private val repository: CategoryRepository): IUseCase {
    override suspend fun getCategories(): Flow<ResourceState<List<Category>>> = repository.getAll()
    override suspend fun getSubCategoriesFromCategory(id: String): Flow<ResourceState<List<SubCategory>>> = repository.getSubcategoriesFromCategory(id)
}