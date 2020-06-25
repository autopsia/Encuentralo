package com.sectordefectuoso.encuentralo.ui.register.service

import android.util.Log
import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import java.lang.Exception

class RegisterServiceViewModel : ViewModel() {
    var idCategory = ""

    val getCategorias = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            CategoryRepository().getAll().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
            Log.e("Error", e.message)
        }
    }

    val getSubCategories: LiveData<ResourceState<List<SubCategory>>> = liveData {
        CategoryRepository().getSubCategoriesByCategory(idCategory).onStart { }.catch { }
            .collect { items -> emit(items) }
    }
}