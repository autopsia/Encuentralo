package com.sectordefectuoso.encuentralo.ui.profile.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.data.repository.UserRepository
import com.sectordefectuoso.encuentralo.domain.user.IUserUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class ProfileViewModel(private val useCase: IUserUC): ViewModel() {
    val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    fun updateEmail(newEmail: String, prevEmail: String, password: String) = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            val result = useCase.updateEmail(newEmail, prevEmail, password)
            emit(result)
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun updateDB(user: User) = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            val result = useCase.update(user)
            emit(result)
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
        }
    }
}