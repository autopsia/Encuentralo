package com.sectordefectuoso.encuentralo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.ui.home.adapter.CategoriesAdapter
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {

    @ExperimentalCoroutinesApi
    private lateinit var homeViewModel: HomeViewModel
    override val TAG: String
        get() = "HomeFragment"

    override fun getLayout(): Int = R.layout.fragment_home

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
              ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        var categories: ArrayList<Category> = arrayListOf(
            Category("Plomero", 1),
            Category("Electricista", 2)
        )

        val rvCategories: RecyclerView = root.findViewById(R.id.rvCategories)
        val rvAdapter = CategoriesAdapter(categories)
        rvCategories.adapter = rvAdapter
        rvCategories.layoutManager = GridLayoutManager(this.context, 3, LinearLayoutManager.VERTICAL, false)

        categories

        /*
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        */
        return root
    }
}
