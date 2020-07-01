package com.sectordefectuoso.encuentralo.ui.home.subcategories

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class SubCategoryViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    categoryRepository: CategoryRepository) : ViewModel() {
    private val state = savedStateHandle

    private val _categoryid : MutableLiveData<String> = state.getLiveData("categoryId")

    fun saveCurrentSubCategory(categoryId: String) {
        // Sets a new value for the object associated to the key.
        state.set("categoryId", categoryId)
    }
    fun getCurrenSubCategory(): String {
        // Gets the current value of the user id from the saved state handle
        return state.get("categoryId")?: ""
    }

    val getSubcategoriesByCategory = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
                categoryRepository.getSubCategoriesByCategory(getCurrenSubCategory()).collect {
                    emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
            Log.e("Error", e.message)
        }
    }

}
