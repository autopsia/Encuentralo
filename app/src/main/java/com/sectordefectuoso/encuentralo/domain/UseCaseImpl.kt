package com.sectordefectuoso.encuentralo.domain

import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class UseCaseImpl @ExperimentalCoroutinesApi constructor(private val repository: CategoryRepository): IUseCase {
    @ExperimentalCoroutinesApi
    override suspend fun getCategories(): Flow<ResourceState<List<Category>>> = repository.getAll()

}