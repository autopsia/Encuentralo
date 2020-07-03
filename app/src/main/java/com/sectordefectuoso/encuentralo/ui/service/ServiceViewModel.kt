package com.sectordefectuoso.encuentralo.ui.service

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class ServiceViewModel(private val serviceUC: IServiceUC) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    val getServices = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            serviceUC.listByUser().collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
            Log.e("Error", e.message)
        }
    }
}