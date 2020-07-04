package com.sectordefectuoso.encuentralo.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.domain.chat.IChatUC
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC
import com.sectordefectuoso.encuentralo.domain.storage.IStorageUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class HistoryViewModel(private val serviceUC: IServiceUC, private val chatUC: IChatUC) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    val serviceIds = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try{
            chatUC.listServicesByUser().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun getServices(ids: List<String>) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try{
            serviceUC.listByIds(ids).collect {
                emit(it)
            }
        } catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }
}