package com.sectordefectuoso.encuentralo.ui.home.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.ui.home.postlist.PostListFragmentDirections
import java.time.LocalDateTime
import java.util.*

class PostListAdapter(private var services : List<Service>) :
    RecyclerView.Adapter<PostListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tvPostListTitle)
        var meta: TextView = itemView.findViewById(R.id.tvPostListMeta)
        var cv: CardView = itemView.findViewById(R.id.cvPostList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_postlist, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //val currentTime = LocalDateTime.now()
        holder.title.text = services[position].title
        holder.meta.text = DateUtils.getRelativeTimeSpanString(services[position].createdDate.time)
        holder.cv.setOnClickListener {
            val service = services[position]
            val action = PostListFragmentDirections.actionPostListFragmentToPostFragment(service.documentId, service.title, service.userId)
            holder.itemView.findNavController().navigate(action)
        }
    }
}