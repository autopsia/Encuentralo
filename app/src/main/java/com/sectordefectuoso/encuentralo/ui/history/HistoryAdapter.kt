package com.sectordefectuoso.encuentralo.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Service

class HistoryAdapter(private val services: ArrayList<Service>): RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lblTitle = itemView.findViewById<TextView>(R.id.lblHistoryTitle)
        var lblDate = itemView.findViewById<TextView>(R.id.lblHistoryDate)
        var btnChat = itemView.findViewById<ImageView>(R.id.btnHistoryChat)
        var ivPhoto = itemView.findViewById<ImageView>(R.id.ivHsitoryPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyViewHolder {
        val  itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    override fun onBindViewHolder(holder: HistoryAdapter.MyViewHolder, position: Int) {
        val service = services[position]
        holder.lblTitle.text = service.title
        holder.lblDate.text = service.description

        FirebaseStorage.getInstance().reference.child("User/${service.userId}.jpg").downloadUrl.addOnSuccessListener { uri ->
            val result = uri.toString()
            Glide.with(holder.itemView).load(result).placeholder(R.drawable.ic_user).error(R.drawable.ic_user).centerCrop().into(holder.ivPhoto)
        }
    }
}