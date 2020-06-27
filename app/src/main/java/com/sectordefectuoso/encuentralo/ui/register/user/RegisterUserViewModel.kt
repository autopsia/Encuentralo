package com.sectordefectuoso.encuentralo.ui.register.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers

class RegisterUserViewModel(private val useCase: IUserUC): ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun saveAuth(email: String, password: String) = liveData(coroutineContext){
        emit(ResourceState.Loading)
        try {
            val result = useCase.createAuth(email, password)
            emit(result)
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun saveDB(user: User) = liveData(coroutineContext){
        emit(ResourceState.Loading)
        try {
            val result = useCase.create(user)
            emit(result)
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }
}
