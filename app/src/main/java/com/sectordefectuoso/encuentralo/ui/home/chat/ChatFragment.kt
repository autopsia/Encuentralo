package com.sectordefectuoso.encuentralo.ui.home.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.ChatMessage
import com.sectordefectuoso.encuentralo.ui.home.adapter.ChatAdapter
import com.sectordefectuoso.encuentralo.ui.home.postlist.PostListFragmentArgs
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChatFragment : BaseFragment() {
    val chatViewModel : ChatViewModel by viewModels()
    val args : PostListFragmentArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override val TAG: String
        get() = "ChatFragment"

    override fun getLayout(): Int {
        return R.layout.fragment_chat
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(getLayout(), container, false)
        auth = FirebaseAuth.getInstance()

        var ibChatSend : ImageButton = root.findViewById(R.id.ibChatSend)
        val ibChatPrice : ImageButton = root.findViewById(R.id.ibChatPrice)

        observeData()

        ibChatSend.setOnClickListener {
            Log.i(TAG, "IBCHATSEND")
            if (etChat.text.isEmpty()){return@setOnClickListener}
            var message:ChatMessage = ChatMessage("", etChat.text.toString(), Date(), auth.uid ?: "", 1)
            Log.i(TAG, message.toString())
            uiScope.launch {
                sendMessage(message)
            }
            etChat.text.clear()
        }

        if (ibChatPrice.visibility == View.VISIBLE)
        ibChatPrice.setOnClickListener {
            Log.i(TAG, "IBCHATSEND")
            if (etChat.text.isEmpty()){return@setOnClickListener}
            //TODO: FALTA VALIDAR SOLO NUMBEROS
            var jsonString = "{\"emitter\": \"${auth.uid}\",\"receptor\": \"\",\"price\": ${etChat.text.toString()},\"isAccepted\": 0}"
            var message:ChatMessage = ChatMessage("", jsonString, Date(), auth.uid ?: "", 2)
            Log.i(TAG, message.toString())
            uiScope.launch {
                sendMessage(message)
            }
            etChat.text.clear()
        }

        return root

    }

    private fun observeData(){
        chatViewModel.getChatMessages.observe(viewLifecycleOwner, Observer {result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i(TAG, result.toString())
                }
                is ResourceState.Success -> {
                    val adapter = ChatAdapter(result.data as ArrayList<ChatMessage>, ibChatPrice)
                    rvChat.adapter = adapter
                    rvChat.scrollToPosition((rvChat.adapter?.itemCount ?: 1) - 1)
                    Log.i(TAG, result.data.toString())
                }
                is ResourceState.Failed -> {
                    Log.e(TAG, result.message)
                }
            }

        })
    }

    private suspend fun sendMessage(message: ChatMessage){
        chatViewModel.settearMessage(message)
        chatViewModel.sendChatMessage().collect { state ->
            when(state){
                is ResourceState.Loading -> {
                    Log.i(TAG, state.toString())
                }
                is ResourceState.Success -> {
                    Log.i(TAG, state.data.toString())
                    rvChat.scrollToPosition((rvChat.adapter?.itemCount ?: 1) - 1)

                }
                is ResourceState.Failed -> {
                    Log.e(TAG, state.message)
                }
            }
        }
    }
}