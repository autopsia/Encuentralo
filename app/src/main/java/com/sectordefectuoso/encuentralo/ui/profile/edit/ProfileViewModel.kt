package com.sectordefectuoso.encuentralo.ui.profile.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.data.repository.UserRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class ProfileViewModel: ViewModel() {
    lateinit var user: User

    val updateEmail = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            UserRepository().updateEmail(user.email).collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
            Log.e("Error", e.message)
        }
    }

    val updateInformation = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            UserRepository().updateInformation(user).collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
            Log.e("Error", e.message)
        }
    }
}