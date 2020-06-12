package com.sectordefectuoso.encuentralo.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.domain.IUseCase
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class HomeViewModel ( useCase: IUseCase) : ViewModel() {

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

}


