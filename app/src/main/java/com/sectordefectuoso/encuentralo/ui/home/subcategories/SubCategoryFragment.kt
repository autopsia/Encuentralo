package com.sectordefectuoso.encuentralo.ui.home.subcategories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.ui.home.adapter.SubCategoriesAdapter
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_subcategories.*

@AndroidEntryPoint
class SubCategoryFragment : BaseFragment() {
    val args: SubCategoryFragmentArgs by navArgs()

    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    //private val subCategoryViewModel by lazy { ViewModelProvider(this,HomeViewModelFactory(UseCaseImpl(CategoryRepository()))).get(SubCategoryViewModel::class.java) }
    override val TAG: String
        get() = "HomeFragment"

    override fun getLayout(): Int = R.layout.fragment_subcategories


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        subCategoryViewModel.saveCurrentSubCategory(args.categoryId)
        val root = inflater.inflate(R.layout.fragment_subcategories, container, false)

        savedInstanceState?.putString("categoryId", args.categoryId)


        val rvSubCategories: RecyclerView = root.findViewById(R.id.rvSubCat)
        rvSubCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        observeData()

        return root
    }

    private fun observeData(){
        subCategoryViewModel.getSubcategoriesByCategory.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i("observable ", "Cargando subcat")
                }
                is ResourceState.Success -> {
                    val subCategorias = result.data
                    Log.i("xxx", subCategorias.toString())
                    val adapter = SubCategoriesAdapter(subCategorias as ArrayList<SubCategory>)
                    rvSubCat.adapter = adapter

                    Log.i("success subcat", "${result.data}")

                }
                is ResourceState.Failed -> {
                    Log.i("failure", "error")

                }
            }
        })
    }
}
