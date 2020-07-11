package com.sectordefectuoso.encuentralo.ui.home.chat

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sectordefectuoso.encuentralo.data.model.ChatMessage
import com.sectordefectuoso.encuentralo.data.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class ChatViewModel @ViewModelInject constructor(
chatRepository: ChatRepository,
@Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state = savedStateHandle
    var chatRepository = chatRepository
    fun getUserId() : String { return state.get("userId") ?: "" }
    fun getSellerId() : String { return state.get("sellerId") ?: "" }
    fun getServiceId() : String { return state.get("serviceId") ?: "" }
    lateinit var message : ChatMessage

    fun settearMessage(_message: ChatMessage){
        message = _message
        Log.i("ChatviewModel",message.toString())
    }
    var message2 : ChatMessage = ChatMessage()

    fun settearMessage2(_message2: ChatMessage){
        message2 = _message2
        Log.i("ChatviewModel",message2.toString())
    }


    val getChatMessages = liveData(Dispatchers.IO){
        chatRepository.getChat(getServiceId(), getUserId()).collect {
            emit(it)
        }
    }

    fun sendChatMessage() = chatRepository.sendMessage(getServiceId(),getUserId(),message)
    fun acceptAgreement() = chatRepository.setAgreement(getServiceId(), message2)
}