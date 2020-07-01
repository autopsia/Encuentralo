package com.sectordefectuoso.encuentralo.data.repository.storage

import android.net.Uri
import com.sectordefectuoso.encuentralo.utils.ResourceState

interface IStorageRepo {
    suspend fun updateImage(uri: Uri, uid: String): ResourceState<String>
}