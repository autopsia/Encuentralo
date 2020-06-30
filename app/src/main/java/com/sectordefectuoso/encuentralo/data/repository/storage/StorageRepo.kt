package com.sectordefectuoso.encuentralo.data.repository.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.tasks.await

class StorageRepo: IStorageRepo {
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("User Images")

    override suspend fun updateImage(uri: Uri, uid: String): ResourceState<String> {
        val result = storageRef.child("${uid}.jpg").putFile(uri).await().storage.downloadUrl.await().toString()
        return ResourceState.Success(result)
    }
}