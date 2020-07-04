package com.sectordefectuoso.encuentralo.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.ui.home.post.ChatList.ChatListFragmentDirections
import com.sectordefectuoso.encuentralo.ui.home.subcategories.SubCategoryFragmentDirections

class ChatListAdapter ( private val chatList : ArrayList<String>, val serviceId: String, val sellerId: String) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {
    private lateinit var auth: FirebaseAuth

    inner class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView = itemView.findViewById(R.id.cvChatList)
        var text: TextView = itemView.findViewById(R.id.tvChatList)
        //var img = itemView.findViewById<ImageView>(R.id.ivHomeCatList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        auth = FirebaseAuth.getInstance()
        val  itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chatlist, parent, false)
        return ChatListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.text.text = "Chat $position"

        holder.cardView.setOnClickListener {
            val list = chatList[position]+1
            val  action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(serviceId , chatList[position] , chatList[position])
            holder.itemView.findNavController().navigate(action)
        }
    }
}