package com.sectordefectuoso.encuentralo.ui.home

import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class HomeViewModel ( private val repository: CategoryRepository ) : ViewModel() {
    private val _categoriesTest = MutableLiveData<Category>().apply {
        value = Category("oscar", 1)
    }
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    /*
    private val _categories = repository
        .getAll()
        .asLiveData(viewModelScope.coroutineContext)


    val categories: LiveData<ResourceState<List<Category>>>
        get() = _categories

        
     */
    val text: LiveData<String> = _text
    val category: LiveData<Category> = _categoriesTest
}