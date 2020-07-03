package com.sectordefectuoso.encuentralo.domain.category

import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.data.repository.category.ICategoryRepo
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

class CategoryUC(private val repo: ICategoryRepo): ICategoryUC {
    override suspend fun getAll(): Flow<ResourceState<List<Category>>> = repo.getAll()

    override suspend fun getSubcategoriesByCategory(idCategory: String): Flow<ResourceState<List<SubCategory>>> = repo.getSubcategoriesByCategory(idCategory)

    override suspend fun getCategoryBySubcategory(idSubCategory: String): Flow<ResourceState<String>> = repo.getCategoryBySubcategory(idSubCategory)
}