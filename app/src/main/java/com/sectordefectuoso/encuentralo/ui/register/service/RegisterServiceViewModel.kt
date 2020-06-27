package com.sectordefectuoso.encuentralo.ui.register.service

import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.domain.category.ICategoryUC
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class RegisterServiceViewModel(private val serviceUC: IServiceUC, private val categoryUC: ICategoryUC) : ViewModel() {
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
}