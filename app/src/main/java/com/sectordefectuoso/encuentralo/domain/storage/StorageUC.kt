package com.sectordefectuoso.encuentralo.domain.storage

import android.net.Uri
import com.sectordefectuoso.encuentralo.data.repository.storage.IStorageRepo
import com.sectordefectuoso.encuentralo.utils.ResourceState

class StorageUC(private val repo: IStorageRepo): IStorageUC {
    override suspend fun updateImage(uri: Uri, uid: String): ResourceState<String> = repo.updateImage(uri, uid)
}