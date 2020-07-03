package com.sectordefectuoso.encuentralo.data.repository.storage

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class StorageRepo: IStorageRepo {
    private val storage = FirebaseStorage.getInstance().reference
    private val storageRef: StorageReference = storage

    override suspend fun updateImage(uri: Uri, uid: String, path: String): Flow<ResourceState<Boolean>> = flow {
        try{
            storageRef.child(path).child("${uid}.jpg").putFile(uri).await()
            emit(ResourceState.Success(true))
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    override suspend fun loadImage(path: String): Flow<ResourceState<String>> = flow{
        val result = storage.child(path).downloadUrl.await().toString()
        emit(ResourceState.Success(result))
    }
}