package com.sectordefectuoso.encuentralo.ui.home.post.ChatList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sectordefectuoso.encuentralo.data.repository.ChatListRepository
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class ChatListViewModel @ViewModelInject constructor(
chatListRepository: ChatListRepository,
@Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    val state = savedStateHandle
    fun getUserId() : String { return state.get("userId") ?: "" }
    fun getSellerId() : String { return state.get("sellerId") ?: "" }
    fun getServiceId() : String { return state.get("serviceId") ?: "" }

    val getChatListByServiceId = liveData(Dispatchers.IO) {
                chatListRepository.getAllServiceChatReferences(getServiceId()).collect {
                    emit(it)
                }
    }
}