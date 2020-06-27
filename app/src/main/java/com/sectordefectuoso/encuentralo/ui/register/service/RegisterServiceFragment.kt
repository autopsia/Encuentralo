package com.sectordefectuoso.encuentralo.ui.register.service

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.data.repository.category.CategoryRepo
import com.sectordefectuoso.encuentralo.data.repository.service.ServiceRepo
import com.sectordefectuoso.encuentralo.domain.category.CategoryUC
import com.sectordefectuoso.encuentralo.domain.category.ICategoryUC
import com.sectordefectuoso.encuentralo.domain.service.ServiceUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.RegisterServiceViewModelFactory
import kotlinx.android.synthetic.main.fragment_register_service.*

class RegisterServiceFragment : BaseFragment() {

    companion object {
        fun newInstance() = RegisterServiceFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this, RegisterServiceViewModelFactory(
                ServiceUC(ServiceRepo()), CategoryUC(
                    CategoryRepo()
                )
            )
        ).get(RegisterServiceViewModel::class.java)
    }
    private var categoryList = listOf<Category>()
    private var subcategoryList = listOf<SubCategory>()

    override val TAG: String get() = "RegisterServiceFragment"

    override fun getLayout(): Int = R.layout.fragment_register_service

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_register_service, container, false)
        setCategories()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cboCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setSubCategories(categoryList[position].documentId)
            }

        }
    }

    private fun setCategories(){
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    categoryList = result.data
                    val items = categoryList.map { it.name }
                    val categoryAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_simple_spinner,
                        items
                    )
                    cboCategory.adapter = categoryAdapter
                    setSubCategories(categoryList[0].documentId)
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_CATEGORY", result.message)
                }
            }
        })
    }

    private fun setSubCategories(idCategory: String) {
        viewModel.getSubCategories(idCategory).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    subcategoryList = result.data
                    if(subcategoryList.size == 0) {
                        subcategoryList += SubCategory("", "Seleccione", 0)
                    }
                    val items = subcategoryList.map { it.name }
                    val subCategoryAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_simple_spinner,
                        items
                    )
                    cboSubcategory.adapter = subCategoryAdapter
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_CATEGORY", result.message)
                }
            }
        })
    }
}