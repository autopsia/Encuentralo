package com.sectordefectuoso.encuentralo.ui.home.post.ChatList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.ChatList
import com.sectordefectuoso.encuentralo.data.model.ChatMessage
import com.sectordefectuoso.encuentralo.ui.home.adapter.ChatAdapter
import com.sectordefectuoso.encuentralo.ui.home.adapter.ChatListAdapter
import com.sectordefectuoso.encuentralo.ui.home.post.PostFragmentArgs
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chatlist.*

@AndroidEntryPoint
class ChatListFragment : BaseFragment() {
    private val args: PostFragmentArgs by navArgs()
    private val chatListViewModel : ChatListViewModel by viewModels()
    override val TAG: String
        get() = "ChatListFragment"

    override fun getLayout(): Int {
        return R.layout.fragment_chatlist
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(getLayout(), container, false)

        observeData()
        return root
    }

    fun observeData(){
        chatListViewModel.getChatListByServiceId.observe( viewLifecycleOwner, Observer { result ->
            when (result){
                is ResourceState.Loading -> {
                    Log.i(TAG, result.toString())
                }
                is ResourceState.Success -> {
                    val chatList : ChatList? = result.data
                    val adapter = ChatListAdapter(chatList?.chatList as ArrayList<String>, args.serviceId, args.userId)
                    rvChatList.adapter = adapter
                    Log.i(TAG + "success", result.data.toString())
                }
                is ResourceState.Failed -> {
                    Log.e(TAG  + "failed" , result.message)
                }
            }
        })
    }
}