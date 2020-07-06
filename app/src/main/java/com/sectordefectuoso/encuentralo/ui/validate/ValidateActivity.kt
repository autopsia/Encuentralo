package com.sectordefectuoso.encuentralo.ui.validate

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.ServiceActivity
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.data.repository.storage.StorageRepo
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.storage.StorageUC
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.utils.Functions
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.ValidateViewModelFactory
import kotlinx.android.synthetic.main.activity_validate.*
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class ValidateActivity : AppCompatActivity() {
    private val IMAGE_PICK_CODE = 1001;
    private val IMAGE_CAPTURE_CODE = 1002;
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ValidateViewModelFactory(UserUC(UserRepo()), StorageUC(StorageRepo()))
        ).get(ValidateViewModel::class.java)
    }

    private var url = ""
    private var validDoc = false
    private var imageUri: Uri? = null
    private var user: User = User()

    private lateinit var alertDialog: AlertDialog
    private lateinit var photoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate)

        setInformation()

        btnValidSearch.setOnClickListener {
            if(txtValidDocument.text.toString().length == 8) {
                Functions.closeKeyBoard(this, it)
                val document = txtValidDocument.text.toString()
                val url = "https://api.reniec.cloud/dni/$document"
                AsyncTaskHandler().execute(url)
            }
        }
        txtValidBirthdate.setOnClickListener {
            Functions.openCalendar(this, txtValidBirthdate)
        }
        btnValidPhoto.setOnClickListener {
            takePhoto()
        }
        btnValidSave.setOnClickListener {
            if(validate()) {
                setUser()
                alertDialog = Functions.createDialog(
                    this,
                    R.layout.alert_dialog_2,
                    "Guardando",
                    "",
                    null
                )!!
                if(imageUri != null) {
                    uploadImage(user)
                }
                else{
                    updateUser(user)
                }
            }
        }
    }

    private fun setInformation() {
        user = Gson().fromJson(intent.extras!!.getString("user"), User::class.java)
        txtValidDocument.setText(user.document)
        txtValidName.setText(user.names)
        txtValidLastName.setText(user.lastNames)
        txtValidBirthdate.setText(SimpleDateFormat("dd/MM/yyyy").format(user.birthdate))
        txtValidPhone.setText(user.phone)
        loadImage(user.documentId)
    }

    private fun loadImage(uid: String) {
        viewModel.loadImage("User/$uid.jpg")
            .observe(this, Observer { result ->
                when (result) {
                    is ResourceState.Loading -> {
                        Log.d("LOADING_PHOTO", "Cargando imagen")
                    }
                    is ResourceState.Success -> {
                        url = result.data
                        Glide.with(this).load(url)
                            .centerCrop().into(ivValidPhoto)
                    }
                    is ResourceState.Failed -> {
                        Log.d("ERROR_PHOTO", result.message)
                    }
                }
            })
    }

    private fun takePhoto() {
        val options = arrayOf<CharSequence>("Cárama", "Galería", "Cancelar")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione")
        builder.setItems(options) { dialog, item ->
            when (item) {
                0 -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    photoFile = getPhoto()
                    val fileProvider = FileProvider.getUriForFile(
                        this,
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

        var storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private fun validate(): Boolean {
        var valid = true
        if (Functions.validateTextView(txtValidName)) valid = false
        if (Functions.validateTextView(txtValidLastName)) valid = false
        if (Functions.validateTextView(txtValidBirthdate)) valid = false
        if (Functions.validateTextView(txtValidPhone)) valid = false

        if (Functions.validateTextView(txtValidDocument, 8)) {
            return showNotification("Validación de Campos", "Ingrese un documento válido")
        }
        if (Functions.validateTextViewDate(txtValidBirthdate, Date(), 6570)) {
            return showNotification("Validación de Campos", "Debes ser mayor de edad")
        }
        if (!validDoc) {
            return showNotification("Validación de Documento", "Debe validar su documento de identidad")
        }
        if (url == "" && imageUri == null) {
            return showNotification("Validación de Imagen", "Debe subir una foto de perfil")
        }

        if (Functions.validateTextView(txtValidPhone, 9)) {
            return showNotification("Validación de Campos", "Ingrese un celular válido")
        }

        return valid
    }

    private fun showNotification(title: String, message: String): Boolean {
        alertDialog = Functions.createDialog(this, R.layout.alert_dialog_1, title, message, null)!!
        return false;
    }

    private fun setUser() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        user.document = txtValidDocument.text.toString()
        user.names = txtValidName.text.toString().trim()
        user.lastNames = txtValidLastName.text.toString().trim()
        user.birthdate = dateFormat.parse(txtValidBirthdate.text.toString())
        user.phone = txtValidPhone.text.toString()
    }

    private fun uploadImage(user: User) {
        viewModel.uploadImage(imageUri!!, user.documentId, "User")
            .observe(this, Observer { result ->
                when (result) {
                    is ResourceState.Success -> {
                        updateUser(user)
                    }
                    is ResourceState.Failed -> {
                        alertDialog.dismiss()
                        Functions.showAlert(
                            this,
                            alertDialog,
                            "Atención",
                            "No se pudo subir su imagen"
                        )
                    }
                }
            })
    }

    private fun updateUser(user: User) {
        viewModel.updateDB(user).observe(this, Observer { result ->
            when (result) {
                is ResourceState.Success -> {
                    alertDialog.dismiss()
                    val intent = Intent(this, ServiceActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_PROFILE", result.message)
                    alertDialog.dismiss()
                    Functions.showAlert(
                        this,
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
                Glide.with(this).load(photoFile).centerCrop().into(ivValidPhoto)
            }
            if (requestCode == IMAGE_PICK_CODE) {
                imageUri = data?.data!!
                Glide.with(this).load(imageUri).centerCrop().into(ivValidPhoto)
            }
        }
    }

    inner class AsyncTaskHandler : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            alertDialog =
                Functions.createDialog(
                    this@ValidateActivity,
                    R.layout.alert_dialog_2,
                    "Validando en RENIEC",
                    "",
                    null
                )!!
        }

        override fun doInBackground(vararg url: String?): String {
            val res: String
            val cn = URL(url[0]).openConnection() as HttpURLConnection
            try {
                cn.connect()
                res = cn.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                cn.disconnect()
            }

            return res
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result != "[]") {
                val jsonObject = JSONObject(result)
                val cui = txtValidCui.text.toString()
                val cuiResult = jsonObject.getString("cui")

                validDoc = if (cui == cuiResult) {
                    btnValidSearch.setColorFilter(Color.parseColor("#25F800"))
                    true
                } else {
                    btnValidSearch.setColorFilter(Color.parseColor("#FF0000"))
                    false
                }
            }

            if (alertDialog.isShowing) alertDialog.dismiss()
        }
    }
}