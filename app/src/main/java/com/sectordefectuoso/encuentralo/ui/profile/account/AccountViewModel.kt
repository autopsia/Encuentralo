package com.sectordefectuoso.encuentralo.ui.profile.account

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.domain.storage.IStorageUC
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class AccountViewModel(private val userCase: IUserUC, private val storageUC: IStorageUC): ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun loadImage(path: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            storageUC.loadImage(path).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun getUser() = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            userCase.get().collect{
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun logout() = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try{
            val result = userCase.logout()
            emit(result)
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }
}