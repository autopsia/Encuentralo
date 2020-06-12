package com.sectordefectuoso.encuentralo.ui.home

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.domain.IUseCase
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.flow.map
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class HomeViewModel ( useCase: IUseCase) : ViewModel() {
    private val mutableCategories = MutableLiveData<List<Category>>()
    private val getCategoriesJob: Job? = null
    val categoriesLiveData = mutableCategories
    private val job = Job()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private val _getCat = MutableLiveData<ResourceState<List<Category>>>()


    val getCategorias = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            useCase.getCategories().collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
            Log.e("Error", e.message)
        }
    }


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


}


