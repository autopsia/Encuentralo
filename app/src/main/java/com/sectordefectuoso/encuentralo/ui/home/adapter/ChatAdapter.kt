package com.sectordefectuoso.encuentralo.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.ChatMessage
import java.util.zip.Inflater

class ChatAdapter ( private val messages: ArrayList<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var auth: FirebaseAuth


    inner class Message1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChatMsg : TextView = itemView.findViewById(R.id.tvChatMsg1)
    }

    inner class Message2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChatMsg : TextView = itemView.findViewById(R.id.tvChatMsg2)
    }

    inner class PriceMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChatPrice : TextView = itemView.findViewById(R.id.tvChatPrice)
        val btnChatPriceAccept : Button = itemView.findViewById(R.id.btnChatPriceAccept)
        val btnChatPriceReject : Button = itemView.findViewById(R.id.btnChatPriceReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var layout:Int = R.layout.item_chat_2
         if (viewType == 1){
             layout = R.layout.item_chat_1
         }
        if (viewType == 3){
             layout = R.layout.item_chat_price
         }

        val itemView = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return when (viewType){
            1 -> {
                Message1ViewHolder(itemView)
            }
            2 -> {
                Message2ViewHolder(itemView)
            }
            3 -> {
                PriceMessageViewHolder(itemView)
            }
            else -> {
                Message2ViewHolder(itemView)
            }
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        auth = FirebaseAuth.getInstance()
        if (messages[position].type == 2) {
            Log.i("ENTRO", messages[position].toString())
            return 3
        }
        if (auth.uid.equals(messages[position].author)){
            return 1
        }
            return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is Message1ViewHolder -> {
                holder.tvChatMsg.text = messages[position].message
            }
            is Message2ViewHolder -> {
                holder.tvChatMsg.text = messages[position].message
            }
            is PriceMessageViewHolder -> {
                holder.tvChatPrice.text = "S/${messages[position].message}"
                holder.btnChatPriceAccept.setOnClickListener {

                }

                holder.btnChatPriceReject.setOnClickListener {

                }
            }
        }
    }
}