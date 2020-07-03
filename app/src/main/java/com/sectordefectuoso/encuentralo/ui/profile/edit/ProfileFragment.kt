package com.sectordefectuoso.encuentralo.ui.profile.edit

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.User
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.data.repository.storage.StorageRepo
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.storage.StorageUC
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.Functions
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.ProfileViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.time.milliseconds

class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val IMAGE_PICK_CODE = 1001;
    private val IMAGE_CAPTURE_CODE = 1002;

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ProfileViewModelFactory(UserUC(UserRepo()), StorageUC(StorageRepo()))
        ).get(ProfileViewModel::class.java)
    }
    private lateinit var alertDialog: AlertDialog
    private var userBundle: User = User()
    private lateinit var photoFile: File
    private var imageUri: Uri? = null

    override val TAG: String get() = "ProfileFragment"

    override fun getLayout(): Int = R.layout.fragment_profile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(getLayout(), container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setInformation()

        txtAccountBirthdate.setOnClickListener {
            Functions.openCalendar(requireContext(), txtAccountBirthdate)
        }
        ivAccountCamera.setOnClickListener {
            takePhoto()
        }
        btnAccountSave.setOnClickListener {
            if (validate()) {
                var user = setUser()
                alertDialog = showAlertDialog(
                    requireContext(),
                    R.layout.alert_dialog_2,
                    "Guardando",
                    "",
                    null
                )
                uploadImage(user)
            }
        }
    }

    private fun setInformation() {
        userBundle = Gson().fromJson(arguments?.getString("user"), User::class.java)
        txtAccountDocument.setText(userBundle.document)
        txtAccountName.setText(userBundle.names)
        txtAccountLastName.setText(userBundle.lastNames)
        txtAccountBirthdate.setText(SimpleDateFormat("dd/MM/yyyy").format(userBundle.birthdate))
        txtAccountPhone.setText(userBundle.phone)
        loadImage(userBundle.documentId)
    }

    private fun loadImage(uid: String) {
        viewModel.loadImage("User/$uid.jpg")
            .observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is ResourceState.Loading -> {
                        Log.d("LOADING_PHOTO", "Cargando imagen")
                    }
                    is ResourceState.Success -> {
                        val url = result.data
                        Glide.with(requireContext()).load(url)
                            .centerCrop().into(ivAccountPhotoE)
                    }
                    is ResourceState.Failed -> {
                        Log.d("ERROR_PHOTO", result.message)
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
        if (Functions.validateTextView(txtAccountDocument)) valid = false
        if (Functions.validateTextView(txtAccountName)) valid = false
        if (Functions.validateTextView(txtAccountLastName)) valid = false
        if (Functions.validateTextView(txtAccountBirthdate)) valid = false
        if (Functions.validateTextView(txtAccountPhone)) valid = false

        if (Functions.validateTextView(
                txtAccountDocument,
                8
            )
        ) return showNotification("Validación de Campos", "Ingrese un documento válido")
        if (Functions.validateTextViewDate(
                txtAccountBirthdate,
                Date(),
                6570
            )
        ) return showNotification("Validación de Campos", "Debes ser mayor de edad")
        if (Functions.validateTextView(
                txtAccountPhone,
                9
            )
        ) return showNotification("Validación de Campos", "Ingrese un celular válido")

        return valid
    }

    private fun showNotification(title: String, message: String): Boolean {
        alertDialog =
            showAlertDialog(requireContext(), R.layout.alert_dialog_1, title, message, null)
        return false;
    }

    private fun setUser(): User {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        var user = User()
        user.document = txtAccountDocument.text.toString()
        user.names = txtAccountName.text.toString()
        user.lastNames = txtAccountLastName.text.toString()
        user.birthdate = dateFormat.parse(txtAccountBirthdate.text.toString())
        user.phone = txtAccountPhone.text.toString()

        return user
    }

    private fun uploadImage(user: User) {
        if (imageUri != null) {
            viewModel.uploadImage(imageUri!!, userBundle.documentId)
                .observe(viewLifecycleOwner, Observer { result ->
                    when (result) {
                        is ResourceState.Success -> {
                            updateUser(user)
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
        } else {
            updateUser(user)
        }
    }

    private fun updateUser(user: User) {
        viewModel.updateDB(user).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Success -> {
                    hideAlertDialog(alertDialog)
                    nav_host_fragment.findNavController()
                        .popBackStack(R.id.navigation_account, false)
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_PROFILE", result.message)
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
                Glide.with(requireActivity()).load(photoFile).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(ivAccountPhotoE)
            }
            if (requestCode == IMAGE_PICK_CODE) {
                imageUri = data?.data!!
                Glide.with(requireActivity()).load(imageUri).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(ivAccountPhotoE)
            }
        }
    }
}