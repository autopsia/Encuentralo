package com.sectordefectuoso.encuentralo.ui.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Service

class ServiceAdapter(private var services: ArrayList<Service>): RecyclerView.Adapter<ServiceAdapter.MyViewHolder>()  {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lblTitle = itemView.findViewById<TextView>(R.id.lblServiceTitle)
        var lblDetail = itemView.findViewById<TextView>(R.id.lblServiceDetail)
        var btnEdit = itemView.findViewById<ImageButton>(R.id.btnServiceEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val service = services[position]
        holder.lblTitle.text = service.title
        holder.lblDetail.text = service.description
        holder.btnEdit.setOnClickListener {
            var bundle = bundleOf("service" to Gson().toJson(service))
            holder.itemView.findNavController().navigate(R.id.action_navigation_service_to_serviceManageFragment,
                bundle)
        }
    }
}