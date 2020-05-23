package com.sectordefectuoso.encuentralo.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sectordefectuoso.encuentralo.LoginActivity
import com.sectordefectuoso.encuentralo.MainActivity
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.ui.home.HomeFragment

class CategoriesAdapter(private var categories: ArrayList<Category>, private var context: Context) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    private lateinit var auth: FirebaseAuth

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView = itemView.findViewById<CardView>(R.id.cvHomeCatList)
        var text = itemView.findViewById<TextView>(R.id.tvHomeCatList)
        var img = itemView.findViewById<ImageView>(R.id.ivHomeCatList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        auth = FirebaseAuth.getInstance()
        val  itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_categories, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text.text = categories[position].name
        holder.cardView.setOnClickListener {
            //Toast.makeText(holder.text.context, "Click: ${categories[position].name}", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            (context as MainActivity).finish()
        }
    }
    
}