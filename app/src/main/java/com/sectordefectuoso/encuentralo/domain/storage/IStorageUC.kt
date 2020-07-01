package com.sectordefectuoso.encuentralo.domain.storage

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IStorageUC {
    suspend fun updateImage(uri: Uri, uid: String): Flow<ResourceState<Boolean>>
    suspend fun loadImage(path: String): Flow<ResourceState<String>>
}