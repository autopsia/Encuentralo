package com.sectordefectuoso.encuentralo.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Agreement
import com.sectordefectuoso.encuentralo.data.model.ChatMessage
import org.json.JSONArray
import org.json.JSONObject
import java.util.zip.Inflater

class ChatAdapter ( private val messages: ArrayList<ChatMessage>, val ibChatPrice: ImageButton) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        val tvChatPriceNotResponse : TextView = itemView.findViewById(R.id.tvChatPriceNotResponse)
        val tvChatPricePropu : TextView = itemView.findViewById(R.id.tvChatPricePropu)
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
                if (messages[position].message.get(0) == '{') {
                    var gson = Gson()
                    Log.i("ddd", messages[position].message)
                    var agreement = gson.fromJson(messages[position].message, Agreement::class.java)

                    holder.tvChatPrice.text = "S/${agreement.price}"

                    when(agreement.isAccepted){
                        0 -> {
                            // NO HA SIDO RESPONDIDO
                        }
                        1 -> {
                            // ACEPTADO
                            holder.btnChatPriceAccept.visibility = View.GONE
                            holder.btnChatPriceReject.visibility = View.GONE
                            holder.tvChatPricePropu.text = "Propuesta Aceptada"
                            ibChatPrice.visibility = View.GONE
                        }
                        2 -> {
                            // RECHAZADO
                            holder.btnChatPriceAccept.visibility = View.GONE
                            holder.btnChatPriceReject.visibility = View.GONE
                            holder.tvChatPricePropu.text = "Propuesta Rechazada"


                        }
                    }

                    if (agreement.emitter == auth.uid){
                        holder.btnChatPriceAccept.visibility = View.INVISIBLE
                        holder.btnChatPriceReject.visibility = View.INVISIBLE
                        holder.tvChatPriceNotResponse.visibility = View.VISIBLE
                        holder.tvChatPricePropu.text = "Has enviado una propuesta de:"
                    } else {
                        if (holder.btnChatPriceAccept.visibility == View.VISIBLE)
                            holder.btnChatPriceAccept.setOnClickListener {
                                Toast.makeText(holder.btnChatPriceAccept.context, "ACEPTADO", Toast.LENGTH_LONG).show()
                            }
                        if (holder.btnChatPriceReject.visibility == View.VISIBLE)
                            holder.btnChatPriceReject.setOnClickListener {
                                Toast.makeText(holder.btnChatPriceAccept.context, "RECHAZADO", Toast.LENGTH_LONG).show()
                            }
                    }
                }

            }
        }
    }

}