package com.sectordefectuoso.encuentralo.ui.home

import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class HomeViewModel ( private val repository: CategoryRepository ) : ViewModel(), CoroutineScope {
    private val job = Job()
    private val _categoriesTest = MutableLiveData<Category>().apply {
        value = Category("oscar", 1)
    }
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private val _getCat = MutableLiveData<ResourceState<List<Category>>>()

/*
    @InternalCoroutinesApi
    fun getCategories() {
        launch {
            val _categories: Flow<ResourceState<List<Category>>> = async(Dispatchers.IO) {
                repository.getAll()
            }.await()
            categories.value = _categories.collect(
                categories.value
            )
        }
    }
*/

    private val _categories = viewModelScope.launch {
        repository.getAll()
    }
    /*

    val categories: LiveData<ResourceState<List<Category>>>
        get() = _categories
*/
    
    val category: LiveData<Category> = _categoriesTest
    val text: LiveData<String> = _text
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}


