package com.sectordefectuoso.encuentralo.data.repository

import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IPostListRepository{
    suspend fun getServiceListBySubCategoryId(subCategoryId: String) : Flow<ResourceState<List<Service>>>
}