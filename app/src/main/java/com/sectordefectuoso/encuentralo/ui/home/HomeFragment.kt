package com.sectordefectuoso.encuentralo.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.repository.CategoryRepository
import com.sectordefectuoso.encuentralo.domain.UseCaseImpl
import com.sectordefectuoso.encuentralo.ui.home.adapter.CategoriesAdapter
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.GridSpacingDecoration
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.HomeViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {
    private val homeViewModel by lazy { ViewModelProvider(this,HomeViewModelFactory(UseCaseImpl(CategoryRepository()))).get(HomeViewModel::class.java) }
    override val TAG: String
        get() = "HomeFragment"

    override fun getLayout(): Int = R.layout.fragment_home

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        observeData()

        var categories: ArrayList<Category> = arrayListOf(
            Category("1", "Reparacion", "", "", 1),
            Category("2", "Belleza", "", "",  2),
            Category("3", "Alimentacion", "", "", 2),
            Category("4", "Educacion", "", "", 2),
            Category("5", "Musica", "", "", 2),
            Category("6", "Diseño", "", "", 2),
            Category("7", "Programación", "", "", 2),
            Category("8", "Escritura", "", "", 2),
            Category("9", "Video", "", "", 2)
        )

        val rvCategories: RecyclerView = root.findViewById(R.id.rvCategories)
        val rvAdapter = CategoriesAdapter(categories)
        rvCategories.adapter = rvAdapter
        rvCategories.layoutManager = GridLayoutManager(this.context, 3, LinearLayoutManager.VERTICAL, false)

        observeData()

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.default_padding)
        rvCategories.addItemDecoration(GridSpacingDecoration(3 , spacingInPixels, true, 0))

        /*
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        */
        return root
    }

    private fun observeData(){
        homeViewModel.getCategorias.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i("observable ", "Cargando")
                }
                is ResourceState.Success -> {
                    val categorias = result.data
                    val adapter = CategoriesAdapter(categorias as ArrayList<Category>)
                    rvCategories.adapter = adapter
                    Log.i("success", "${result.data}")
                    //Toast.makeText(this, "${result.data}", 1).show()
                }
                is ResourceState.Failed -> {
                    Log.i("failure", "error")

                }
            }
        })
    }
}
