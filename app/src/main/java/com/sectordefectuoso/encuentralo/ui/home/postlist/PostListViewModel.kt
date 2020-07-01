package com.sectordefectuoso.encuentralo.ui.home.postlist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sectordefectuoso.encuentralo.data.repository.PostListRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class PostListViewModel @ViewModelInject constructor(
    postListRepository: PostListRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var state = savedStateHandle
    fun savSubCategoryId(subCategoryId: String) {
        // Sets a new value for the object associated to the key.
        state.set("subCategoryId", subCategoryId)
    }
    fun getSubCategoryId(): String {
        // Gets the current value of the user id from the saved state handle
        return state.get("subCategoryId")?: ""
    }

    val getPostListBySubCategoryId = liveData(Dispatchers.IO) {
        emit(ResourceState.Loading)
        try {
            postListRepository.getServiceListBySubCategoryId(getSubCategoryId()).collect {
                emit(it)
            }
        } catch (e : Exception){
            emit(ResourceState.Failed(e.message.toString()))
        }
    }


}