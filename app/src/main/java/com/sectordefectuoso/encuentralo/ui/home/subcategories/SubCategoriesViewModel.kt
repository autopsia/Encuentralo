package com.sectordefectuoso.encuentralo.ui.home.subcategories

import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.*

import kotlin.coroutines.CoroutineContext

class SubCategoriesViewModel (private val repository: CategoryRepository ) : ViewModel(), CoroutineScope {
    private val job = Job()
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private val _getCat = MutableLiveData<ResourceState<List<Category>>>()

    val text: LiveData<String> = _text
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}


