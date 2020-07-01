package com.sectordefectuoso.encuentralo.ui.home.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.ui.home.HomeFragmentDirections
import com.sectordefectuoso.encuentralo.ui.home.subcategories.SubCategoryFragmentDirections


class SubCategoriesAdapter(private var subCategories: ArrayList<SubCategory>) : RecyclerView.Adapter<SubCategoriesAdapter.MyViewHolder>() {
    private lateinit var auth: FirebaseAuth

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView = itemView.findViewById<CardView>(R.id.cvSubCat)
        var text = itemView.findViewById<TextView>(R.id.tvSubCatName)
        //var img = itemView.findViewById<ImageView>(R.id.ivHomeCatList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        auth = FirebaseAuth.getInstance()
        val  itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subcat, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text.text = subCategories[position].name

        holder.cardView.setOnClickListener {
            Toast.makeText(holder.text.context, "Click: ${subCategories[position].name}", Toast.LENGTH_SHORT).show()

            val categoryId = subCategories[position].documentId
            val  action = SubCategoryFragmentDirections.actionSubCategoryFragmentToPostListFragment()
            holder.itemView.findNavController().navigate(action)
        }
    }
    
}