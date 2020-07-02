package com.sectordefectuoso.encuentralo.ui.home.chat

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sectordefectuoso.encuentralo.data.repository.ChatRepository

class ChatViewModel @ViewModelInject constructor(
chatRepository: ChatRepository,
@Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

}