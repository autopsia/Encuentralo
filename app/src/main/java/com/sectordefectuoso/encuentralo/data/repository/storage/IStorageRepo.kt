package com.sectordefectuoso.encuentralo.data.repository.storage

import android.net.Uri
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IStorageRepo {
    suspend fun updateImage(uri: Uri, uid: String): Flow<ResourceState<Boolean>>
    suspend fun loadImage(path: String): Flow<ResourceState<String>>
}