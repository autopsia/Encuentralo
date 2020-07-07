package com.sectordefectuoso.encuentralo.ui.service_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.domain.chat.IChatUC
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.util.ArrayList

class ServiceHistoryViewModel(private val userUC: IUserUC, private val serviceUC: IServiceUC, private val chatUC: IChatUC) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun getServiceIds() = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            serviceUC.listByUser().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun getUserIds(serviceIds: ArrayList<String>) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try{
            chatUC.listUsersByService(serviceIds).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun getData(map: ArrayList<Map<String, String>>) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try{
            chatUC.listData(map).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }
}