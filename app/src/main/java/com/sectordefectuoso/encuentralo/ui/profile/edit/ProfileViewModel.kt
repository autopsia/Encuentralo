package com.sectordefectuoso.encuentralo.ui.profile.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.domain.storage.IStorageUC
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class ProfileViewModel(private val userCase: IUserUC, private val storageUC: IStorageUC): ViewModel() {
    val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun uploadImage(uri: Uri, uid: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            val result = storageUC.updateImage(uri, uid)
            emit(result)
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun updateDB(user: User) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            userCase.update(user).collect{
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }
}