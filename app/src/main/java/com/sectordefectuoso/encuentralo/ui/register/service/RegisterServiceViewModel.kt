package com.sectordefectuoso.encuentralo.ui.register.service

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class RegisterServiceViewModel : ViewModel() {
    var idCategory = ""

    val getCategorias = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            CategoryRepository().getAll().collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
            Log.e("Error", e.message)
        }
    }

    val getSubCategories = liveData(Dispatchers.IO){
        emit(ResourceState.Loading)
        try {
            CategoryRepository().getSubcategoriesFromCategory(idCategory).collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
            Log.e("Error", e.message)
        }
    }
}