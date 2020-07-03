package com.sectordefectuoso.encuentralo.ui.service_edit

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.MainActivity
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.data.repository.category.CategoryRepo
import com.sectordefectuoso.encuentralo.data.repository.service.ServiceRepo
import com.sectordefectuoso.encuentralo.data.repository.storage.StorageRepo
import com.sectordefectuoso.encuentralo.domain.category.CategoryUC
import com.sectordefectuoso.encuentralo.domain.service.ServiceUC
import com.sectordefectuoso.encuentralo.domain.storage.StorageUC
import com.sectordefectuoso.encuentralo.ui.register.service.RegisterServiceViewModel
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.Functions
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.RegisterServiceViewModelFactory
import com.sectordefectuoso.encuentralo.viewmodel.ServiceManageViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_service.*
import kotlinx.android.synthetic.main.fragment_register_service.*
import kotlinx.android.synthetic.main.fragment_service_manage.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log
import kotlin.reflect.typeOf

class ServiceManageFragment : BaseFragment() {

    companion object {
        fun newInstance() = ServiceManageFragment()
    }

    private var categories = ArrayList<Category>()
    private var subcategories = ArrayList<SubCategory>()
    private var service = Service()
    private var idCategory = ""
    private var idSubCategory = ""
    private var init = 0
    private val IMAGE_PICK_CODE = 1001;
    private val IMAGE_CAPTURE_CODE = 1002;
    private var imageUri: Uri? = null
    private lateinit var photoFile: File
    private lateinit var alertDialog: AlertDialog

    private val viewModel by lazy {
        ViewModelProvider(
            this, ServiceManageViewModelFactory(
                ServiceUC(ServiceRepo()),
                CategoryUC(CategoryRepo()),
                StorageUC(StorageRepo())
            )
        ).get(ServiceManageViewModel::class.java)
    }

    override val TAG: String get() = "ServiceManageFragment"

    override fun getLayout(): Int = R.layout.fragment_service_manage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(getLayout(), container, false)
        val cboServCategory = root.findViewById<Spinner>(R.id.cboServCategory)
        val btnServPhoto = root.findViewById<CardView>(R.id.btnServPhoto)
        val btnServSave = root.findViewById<Button>(R.id.btnServSave)

        loadService(root)
        if(idSubCategory != null) {
            getIdCategory(idSubCategory)
        }
        else{
            setCategories()
        }

        cboServCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setSubcategories(categories[position].documentId)
            }

        }
        btnServPhoto.setOnClickListener {
            takePhoto()
        }
        btnServSave.setOnClickListener {
            if(validate()) {
                val data = setService()
                createService(data)
            }
        }

        return root
    }

    private fun loadService(root: View) {
        val args = arguments?.getString("service")
        if(args != null) {
            service = Gson().fromJson(args, Service::class.java)
            val txtTitle = root.findViewById<EditText>(R.id.txtServTitle)
            val txtDescription = root.findViewById<EditText>(R.id.txtServDescription)
            idSubCategory = service.subcategoryId

            txtTitle.setText(service.title)
            txtDescription.setText(service.description)
        }
    }

    private fun setCategories(){
        viewModel.getCategories.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.d("LOADING", "Cargando...")
                }
                is ResourceState.Success -> {
                    categories = result.data as ArrayList<Category>
                    val items = categories.map { it.name }
                    val categoryAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_simple_spinner,
                        items
                    )

                    cboServCategory.adapter = categoryAdapter
                    var index = 0
                    if(idCategory != "") {
                        index = categories.map {it.documentId}.indexOf(idCategory)
                    }
                    cboServCategory.setSelection(index)
                    setSubcategories(categories[index].documentId)
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_CATEGORY", result.message)
                }
            }
        })
    }

    private fun setSubcategories(idCategory: String){
        viewModel.getSubCategories(idCategory).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.d("LOADING", "Cargando...")
                }
                is ResourceState.Success -> {
                    subcategories = result.data as ArrayList<SubCategory>
                    if(subcategories.isEmpty()){
                        subcategories.add(SubCategory("", "Seleccione", 0))
                    }
                    val items = subcategories.map { it.name }
                    val subcategoryAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_simple_spinner,
                        items
                    )

                    cboServSubcategory.adapter = subcategoryAdapter
                    if(idSubCategory != "" && init == 0) {
                        var index = subcategories.map {it.documentId}.indexOf(idSubCategory)
                        if(index == -1) { index = 0 }
                        cboServSubcategory.setSelection(index)
                    }
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_SUBCATEGORY", result.message)
                }
            }
        })
    }

    private fun getIdCategory(name: String) {
        viewModel.getCategory(name).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.d("LOADING", "Cargando...")
                }
                is ResourceState.Success -> {
                    idCategory = result.data
                    setCategories()
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_CATEGORY", result.message)
                }
            }
        })
    }

    private fun takePhoto() {
        val options = arrayOf<CharSequence>("Cárama", "Galería", "Cancelar")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleccione")
        builder.setItems(options) { dialog, item ->
            when (item) {
                0 -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    photoFile = getPhoto()
                    val fileProvider = FileProvider.getUriForFile(
                        requireContext(),
                        "com.sectordefectuoso.encuentralo.fileprovider",
                        photoFile
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                    startActivityForResult(intent, IMAGE_CAPTURE_CODE)
                }
                1 -> {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, IMAGE_PICK_CODE)
                }
                2 -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun getPhoto(): File {
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd-HHmmss").format(Date())
        val fileName = "IMG_${timeStamp}"

        var storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private fun validate(): Boolean {
        var valid = true
        if (Functions.validateSpinner(cboServSubcategory)) valid = false
        if (Functions.validateTextView(txtServTitle, 15)) valid = false
        if (Functions.validateTextView(txtServDescription, 20)) valid = false

        return valid
    }

    private fun setService(): Service {
        var data = Service()
        data.title = txtServTitle.text.toString().trim()
        data.description = txtServDescription.text.toString().trim()
        data.subcategoryId = subcategories[cboServSubcategory.selectedItemPosition].documentId

        if(service != null) {
            data.documentId = service.documentId
        }

        return data
    }

    private fun createService(service: Service) {
        viewModel.saveDB(service).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Loading -> {
                    alertDialog = showAlertDialog(
                        requireContext(),
                        R.layout.alert_dialog_2,
                        "Guardando",
                        "",
                        null
                    )
                }
                is ResourceState.Success -> {
                    if(imageUri != null) {
                        uploadImage(result.data)
                    }
                    else{
                        goBack()
                    }
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_DB", result.message)
                    hideAlertDialog(alertDialog)
                    Functions.showAlert(
                        requireActivity(),
                        alertDialog,
                        "Atención",
                        Functions.DB_FAIL
                    )
                }
            }
        })
    }

    private fun uploadImage(uid: String) {
        viewModel.uploadImage(imageUri!!, uid, "Service").observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Success -> {
                    goBack()
                }
                is ResourceState.Failed -> {
                    hideAlertDialog(alertDialog)
                    Functions.showAlert(
                        requireActivity(),
                        alertDialog,
                        "Atención",
                        "No se pudo subir su imagen"
                    )
                }
            }
        })
    }

    private fun goBack() {
        hideAlertDialog(alertDialog)
        nav_host_service.findNavController()
            .popBackStack(R.id.navigation_service, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_CODE) {
                imageUri = photoFile.toUri()
                Glide.with(requireActivity()).load(photoFile).centerCrop().into(ivServPhoto)
            }
            if (requestCode == IMAGE_PICK_CODE) {
                imageUri = data?.data!!
                Glide.with(requireActivity()).load(imageUri).centerCrop().into(ivServPhoto)
            }
        }
    }
}