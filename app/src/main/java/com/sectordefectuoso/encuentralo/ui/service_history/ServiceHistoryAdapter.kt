package com.sectordefectuoso.encuentralo.ui.service_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.ui.history.HistoryFragmentDirections

class ServiceHistoryAdapter(private val maps: ArrayList<Map<String, Any>>): RecyclerView.Adapter<ServiceHistoryAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lblTitle = itemView.findViewById<TextView>(R.id.lblHistoryTitle)
        var lblDate = itemView.findViewById<TextView>(R.id.lblHistoryDate)
        var btnChat = itemView.findViewById<ImageView>(R.id.btnHistoryChat)
        var ivPhoto = itemView.findViewById<ImageView>(R.id.ivHsitoryPhoto)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceHistoryAdapter.MyViewHolder {
        val  itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return maps.size
    }

    override fun onBindViewHolder(holder: ServiceHistoryAdapter.MyViewHolder, position: Int) {
        val map = maps[position]
        val user = map["user"] as User
        val service = map["service"] as Service

        holder.lblTitle.text = "${user.names} ${user.lastNames}"
        holder.lblDate.text = "Servicio: ${service.description}"
        holder.btnChat.setOnClickListener {
            val action = ServiceHistoryFragmentDirections.actionNavigationServiceHistorialToChatFragment2(service.documentId, service.title, user.documentId)
            holder.itemView.findNavController().navigate(action)
        }

        FirebaseStorage.getInstance().reference.child("User/${user.documentId}.jpg").downloadUrl.addOnSuccessListener { uri ->
            val result = uri.toString()
            Glide.with(holder.itemView).load(result).placeholder(R.drawable.ic_user).error(R.drawable.ic_user).centerCrop().into(holder.ivPhoto)
        }
    }


}