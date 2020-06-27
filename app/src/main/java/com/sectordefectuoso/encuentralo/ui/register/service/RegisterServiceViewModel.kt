package com.sectordefectuoso.encuentralo.ui.register.service

import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.domain.category.ICategoryUC
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class RegisterServiceViewModel(private val serviceUC: IServiceUC, private val categoryUC: ICategoryUC, private val userUC: IUserUC) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun getCategories() = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            categoryUC.getAll().collect{
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun getSubCategories(idCategory: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            categoryUC.getSubcategoriesByCategory(idCategory).collect{
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun saveAuth(email: String, password: String) = liveData(coroutineContext){
        emit(ResourceState.Loading)
        try {
            val result = userUC.createAuth(email, password)
            emit(result)
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun saveUserDB(user: User) = liveData(coroutineContext){
        emit(ResourceState.Loading)
        try {
            val result = userUC.create(user)
            emit(result)
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun saveDB(service: Service) = liveData(coroutineContext){
        emit(ResourceState.Loading)
        try {
            val result = serviceUC.create(service)
            emit(result)
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }
}