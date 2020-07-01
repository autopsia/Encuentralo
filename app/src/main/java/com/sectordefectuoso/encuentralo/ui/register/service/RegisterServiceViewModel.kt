package com.sectordefectuoso.encuentralo.ui.register.service

import android.net.Uri
import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.domain.category.ICategoryUC
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC
import com.sectordefectuoso.encuentralo.domain.storage.IStorageUC
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class RegisterServiceViewModel(
    private val serviceUC: IServiceUC,
    private val categoryUC: ICategoryUC,
    private val userUC: IUserUC,
    private val storageUC: IStorageUC
) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun getCategories() = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            categoryUC.getAll().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun getSubCategories(idCategory: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            categoryUC.getSubcategoriesByCategory(idCategory).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun saveAuth(email: String, password: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            val result = userUC.createAuth(email, password)
            emit(result)
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun saveUserDB(user: User) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            userUC.create(user).collect{
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun uploadImage(uri: Uri, uid: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            val result = storageUC.updateImage(uri, uid)
            emit(result)
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun saveDB(service: Service) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            serviceUC.create(service).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }
}