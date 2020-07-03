package com.sectordefectuoso.encuentralo.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.ChatMessage
import java.util.zip.Inflater

class ChatAdapter ( private val messages: ArrayList<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var auth: FirebaseAuth

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    inner class Message1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChatMsg : TextView = itemView.findViewById(R.id.tvChatMsg1)
    }

    inner class Message2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChatMsg : TextView = itemView.findViewById(R.id.tvChatMsg2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var layout:Int = R.layout.item_chat_2
         if (viewType == 1){
             layout = R.layout.item_chat_1
         }

        val itemView = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        if (viewType == 1){
            return Message1ViewHolder(itemView)
        } else {
            return Message2ViewHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        auth = FirebaseAuth.getInstance()
        if (auth.uid.equals(messages[position].author)){
            return 1
        }
            return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Message1ViewHolder){
            holder.tvChatMsg.text = messages[position].message
        } else if (holder is Message2ViewHolder){
            holder.tvChatMsg.text = messages[position].message
        }
    }
}