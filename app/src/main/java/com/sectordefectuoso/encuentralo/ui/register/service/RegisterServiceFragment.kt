package com.sectordefectuoso.encuentralo.ui.register.service

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.android.synthetic.main.fragment_register_service.*

class RegisterServiceFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterServiceFragment()
    }

    private val registerServiceViewModel by lazy { ViewModelProvider(this).get(RegisterServiceViewModel::class.java)}
    private var categoryList = listOf<Category>()
    private var subcategoryList = listOf<SubCategory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_register_service, container, false)
        loadCategories()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        cboCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                loadSubcategories(categoryList[position].documentId)
            }

        }
    }

    private fun loadCategories(){
        registerServiceViewModel.getCategorias.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i("categorias ", "Cargando")
                }
                is ResourceState.Success -> {
                    categoryList = result.data
                    val categoryItems = categoryList.map { it.name }
                    val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryItems)
                    cboCategory.adapter = categoryAdapter
                    loadSubcategories(categoryList[0].documentId)

                    Log.i("success", "${result.data}")
                    //Toast.makeText(this, "${result.data}", 1).show()
                }
                is ResourceState.Failed -> {
                    Log.i("failure", "error")
                }
            }
        })
    }

    private fun loadSubcategories(idCategory: String){
        registerServiceViewModel.idCategory = idCategory
        registerServiceViewModel.getSubCategories.observe(viewLifecycleOwner, Observer { result ->
            when (result){
                is ResourceState.Loading -> {
                    Log.i("subcategorias ", "Cargando")
                }
                is ResourceState.Success -> {
                    subcategoryList = result.data
                    val subcategoryItems = subcategoryList.map { it.name }
                    val subcategoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, subcategoryItems)
                    cboSubcategory.adapter = subcategoryAdapter
                }
                is ResourceState.Failed -> {
                    Log.i("failure", "error")
                }
            }
        })
    }
}