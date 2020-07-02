package com.sectordefectuoso.encuentralo.ui.home.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.ui.home.postlist.PostListFragmentArgs
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment() {
    val chatViewModel : ChatViewModel by viewModels()
    val args : PostListFragmentArgs by navArgs()

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

        return root
    }
}