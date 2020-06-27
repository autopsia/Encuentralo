package com.sectordefectuoso.encuentralo.ui.profile.account

import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class AccountViewModel(private val useCase: IUserUC): ViewModel() {
    val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun getUser() = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            useCase.get().collect{
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun logout() = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try{
            val result = useCase.logout()
            emit(result)
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }
}