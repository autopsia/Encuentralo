package com.sectordefectuoso.encuentralo.ui.register.service

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.category.CategoryUC
import com.sectordefectuoso.encuentralo.domain.service.ServiceUC
import com.sectordefectuoso.encuentralo.domain.storage.StorageUC
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.Functions
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.RegisterServiceViewModelFactory
import kotlinx.android.synthetic.main.fragment_register_service.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RegisterServiceFragment : BaseFragment() {

    companion object {
        fun newInstance() = RegisterServiceFragment()
    }

    private val IMAGE_PICK_CODE = 1001;
    private val IMAGE_CAPTURE_CODE = 1002;

    private val viewModel by lazy {
        ViewModelProvider(
            this, RegisterServiceViewModelFactory(
                ServiceUC(ServiceRepo()),
                CategoryUC(CategoryRepo()),
                UserUC(UserRepo()),
                StorageUC(StorageRepo())
            )
        ).get(RegisterServiceViewModel::class.java)
    }
    private lateinit var alertDialog: AlertDialog
    private var categoryList = listOf<Category>()
    private var subcategoryList = listOf<SubCategory>()
    private lateinit var photoFile: File
    private lateinit var imageUri: Uri

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

        cboServiceCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        btnServicePhoto.setOnClickListener {
            takePhoto()
        }
        btnServiceSave.setOnClickListener {
            if (validate()) {
                val user = setUser()
                val service = setService()
                createAuthentication(user, service)
            }
        }
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

    private fun setCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
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
                    cboServiceCategory.adapter = categoryAdapter
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
            when (result) {
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    subcategoryList = result.data
                    if (subcategoryList.size == 0) {
                        subcategoryList += SubCategory("", "Seleccione", 0)
                    }
                    val items = subcategoryList.map { it.name }
                    val subCategoryAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_simple_spinner,
                        items
                    )
                    cboServiceSubcategory.adapter = subCategoryAdapter
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_CATEGORY", result.message)
                }
            }
        })
    }

    private fun validate(): Boolean {
        var valid = true
        if (Functions.validateSpinner(cboServiceCategory)) valid = false
        if (Functions.validateSpinner(cboServiceSubcategory)) valid = false
        if (Functions.validateTextView(txtServiceTitle, 15)) valid = false
        if (Functions.validateTextView(txtServiceDescription, 20)) valid = false

        return valid
    }

    private fun setUser(): User {
        return Gson().fromJson(arguments?.getString("user"), User::class.java)
    }

    private fun setService(): Service {
        var service = Service()
        service.title = txtServiceTitle.text.toString().trim()
        service.description = txtServiceDescription.text.toString().trim()
        service.subcategoryId = subcategoryList[cboServiceSubcategory.selectedItemPosition].documentId

        return service
    }

    private fun createAuthentication(user: User, service: Service) {
        viewModel.saveAuth(user.email, user.password)
            .observe(viewLifecycleOwner, Observer { result ->
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
                        user.documentId = result.data
                        uploadImage(user, service)
                    }
                    is ResourceState.Failed -> {
                        Functions.showAlert(
                            requireActivity(),
                            alertDialog,
                            "Atención",
                            Functions.AUTH_FAIL
                        )
                    }
                }
            })
    }

    private fun uploadImage(user: User, service: Service) {
        viewModel.uploadImage(imageUri, user.documentId).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Success -> {
                    user.imageUrl = result.data
                    createUser(user, service)
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

    private fun createUser(user: User, service: Service) {
        viewModel.saveUserDB(user).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Success -> {
                    service.userId = user.documentId
                    createService(service)
                }
                is ResourceState.Failed -> {
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

    private fun createService(service: Service) {
        viewModel.saveDB(service).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Success -> {
                    hideAlertDialog(alertDialog)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                is ResourceState.Failed -> {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_CODE) {
                imageUri = photoFile.toUri()
                Glide.with(requireActivity()).load(photoFile).centerCrop().into(ivServicePhoto)
            }
            if (requestCode == IMAGE_PICK_CODE) {
                imageUri = data?.data!!
                Glide.with(requireActivity()).load(imageUri).centerCrop().into(ivServicePhoto)
            }
        }
    }
}