package com.sectordefectuoso.encuentralo.ui.home.post

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sectordefectuoso.encuentralo.data.repository.PostListRepository
import com.sectordefectuoso.encuentralo.data.repository.storage.StorageRepo
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class PostViewModel @ViewModelInject constructor(
    postListRepository: PostListRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var state = savedStateHandle

    fun savServiceId(serviceId : String){
        state.set("serviceId", serviceId)
    }
    fun getServiceId(): String {
        return state.get("serviceId") ?: ""
    }

    fun savUserId(userId : String){
        state.set("userId", userId)
    }

    fun getUserId(): String {
        return state.get("userId") ?: ""
    }

    val getServiceById = liveData(Dispatchers.IO) {
        emit( ResourceState.Loading )
        try {
            postListRepository.getServiceById(getServiceId()).collect {
                emit(it)
            }

        } catch (e:Exception){
            emit( ResourceState.Failed(e.message.toString()))
        }
    }

    val getUserById = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            postListRepository.getUserById(getUserId()).collect {
                emit(it)
            }
        } catch (e:Exception){
            emit(ResourceState.Failed(e.message.toString()))
        }
    }
    val getImage = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {

            postListRepository.loadImage("${getUserId()}.jpg").collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }
}