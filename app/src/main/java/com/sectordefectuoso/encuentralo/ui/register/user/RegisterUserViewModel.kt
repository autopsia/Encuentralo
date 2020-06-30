package com.sectordefectuoso.encuentralo.ui.register.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect

class RegisterUserViewModel(private val userCase: IUserUC): ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun saveAuth(email: String, password: String) = liveData(coroutineContext){
        emit(ResourceState.Loading)
        try {
            val result = userCase.createAuth(email, password)
            emit(result)
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun saveDB(user: User) = liveData(coroutineContext){
        emit(ResourceState.Loading)
        try {
            userCase.create(user).collect{
                emit(it)
            }
        }
        catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }
}
