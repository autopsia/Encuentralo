package com.sectordefectuoso.encuentralo.ui.service_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.domain.chat.IChatUC
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class ServiceHistoryViewModel(private val userUC: IUserUC, private val chatUC: IChatUC) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun getUserIds(serviceId: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try{
            chatUC.listUsersByService(serviceId).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }
}