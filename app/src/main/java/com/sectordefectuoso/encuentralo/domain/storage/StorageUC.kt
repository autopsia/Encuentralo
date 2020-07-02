package com.sectordefectuoso.encuentralo.domain.storage

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.sectordefectuoso.encuentralo.data.repository.storage.IStorageRepo
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

class StorageUC(private val repo: IStorageRepo): IStorageUC {
    override suspend fun updateImage(uri: Uri, uid: String): Flow<ResourceState<Boolean>> = repo.updateImage(uri, uid)
    override suspend fun loadImage(path: String): Flow<ResourceState<String>> = repo.loadImage(path)
}