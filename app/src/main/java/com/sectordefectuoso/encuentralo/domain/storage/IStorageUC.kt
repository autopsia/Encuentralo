package com.sectordefectuoso.encuentralo.domain.storage

import android.net.Uri
import com.sectordefectuoso.encuentralo.utils.ResourceState

interface IStorageUC {
    suspend fun updateImage(uri: Uri, uid: String): ResourceState<String>
}