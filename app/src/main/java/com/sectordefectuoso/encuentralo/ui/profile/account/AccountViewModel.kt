package com.sectordefectuoso.encuentralo.ui.profile.account

import android.util.Log
import androidx.lifecycle.*
import com.sectordefectuoso.encuentralo.data.repository.UserRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class AccountViewModel : ViewModel() {
    val getUser = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            UserRepository().getUserByUid().collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(ResourceState.Failed(e.message!!))
            Log.e("Error", e.message)
        }
    }
}