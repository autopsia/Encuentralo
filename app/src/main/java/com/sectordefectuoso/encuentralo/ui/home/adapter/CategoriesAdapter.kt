package com.sectordefectuoso.encuentralo.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Category

class CategoriesAdapter(var arraylist: ArrayList<Category>) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    private var categories: List<Category> = ArrayList()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text = itemView.findViewById<TextView>(R.id.tvHomeCatList)
        var img = itemView.findViewById<ImageView>(R.id.ivHomeCatList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_categories, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text.text = categories[position].name
        holder.text.setOnClickListener {
        }
    }

    fun setCategoriesList(categories: List<Category>){
        this.categories = categories
    }
}