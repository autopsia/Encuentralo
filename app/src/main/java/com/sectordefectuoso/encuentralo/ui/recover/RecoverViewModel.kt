package com.sectordefectuoso.encuentralo.ui.recover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers

class RecoverViewModel(private val useCase: IUserUC) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun updatePassword(email: String) = liveData(coroutineContext){
        emit(ResourceState.Loading)
        try {
            val result = useCase.updatePassword(email)
            emit(result)
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }
}
