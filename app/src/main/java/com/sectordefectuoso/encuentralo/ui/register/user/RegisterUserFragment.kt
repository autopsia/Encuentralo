package com.sectordefectuoso.encuentralo.ui.register.user

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.MainActivity

import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.Functions
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.RegisterUserViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_register_user.*
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class RegisterUserFragment : BaseFragment() {

    companion object {
        fun newInstance() = RegisterUserFragment()
    }

    private val IMAGE_PICK_CODE = 1001;
    private val IMAGE_CAPTURE_CODE = 1002;
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            RegisterUserViewModelFactory(UserUC(UserRepo()))
        ).get(RegisterUserViewModel::class.java)
    }

    private var type = ""
    private var validDoc = false
    private var imageUri: Uri? = null

    private lateinit var alertDialog: AlertDialog
    private lateinit var photoFile: File

    override val TAG: String get() = "RegisterUserFragment"

    override fun getLayout(): Int = R.layout.fragment_register_user

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        type = arguments?.getString("type").toString()
        val root = inflater.inflate(getLayout(), container, false)
        showInputs(root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnUserSearch.setOnClickListener {
            if (txtUserDocument.text.toString().length == 8) {
                Functions.closeKeyBoard(requireActivity(), requireView())
                val document = txtUserDocument.text.toString()
                val url = "https://api.reniec.cloud/dni/$document"
                AsyncTaskHandler().execute(url)
            }
        }
        txtUserBirthdate.setOnClickListener {
            Functions.openCalendar(requireContext(), txtUserBirthdate)
        }
        btnUserPhoto.setOnClickListener {
            takePhoto()
        }
        btnUserRegister.setOnClickListener {
            if (validate()) {
                var user = setUser()

                if (type == "2") {
                    var bundle =
                        bundleOf("user" to Gson().toJson(user), "imageUser" to imageUri.toString())
                    nav_login_fragment.findNavController().navigate(
                        R.id.action_registerUserFragment_to_registerServiceFragment,
                        bundle
                    )
                } else {
                    createAuthentication(user)
                }
            }
        }
    }

    private fun showInputs(root: View) {
        val txtBirthdate = root.findViewById<EditText>(R.id.txtUserBirthdate)
        if (type == "1") {
            val lblDni = root.findViewById<TextView>(R.id.textView8)
            val txtDni = root.findViewById<EditText>(R.id.txtUserDocument)
            val lblCui = root.findViewById<TextView>(R.id.textView35)
            val txtCui = root.findViewById<EditText>(R.id.txtUserCui)
            val btnSearch = root.findViewById<ImageButton>(R.id.btnUserSearch)
            val lblBirthdate = root.findViewById<TextView>(R.id.textView11)
            val lblPhoto = root.findViewById<TextView>(R.id.textView36)
            val ivPhoto = root.findViewById<CardView>(R.id.cardView7)
            val btnPhoto = root.findViewById<CardView>(R.id.btnUserPhoto)

            lblDni.visibility = View.GONE
            txtDni.visibility = View.GONE
            lblCui.visibility = View.GONE
            txtCui.visibility = View.GONE
            btnSearch.visibility = View.GONE
            lblBirthdate.visibility = View.GONE
            txtBirthdate.visibility = View.GONE
            lblPhoto.visibility = View.GONE
            ivPhoto.visibility = View.GONE
            btnPhoto.visibility = View.GONE
        } else {
            val btnNext = root.findViewById<Button>(R.id.btnUserRegister)
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentDate = sdf.format(Date())
            txtBirthdate.setText(currentDate)
            btnNext.text = "Siguiente"
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

    private fun validate(): Boolean {
        var valid = true
        if (Functions.validateTextView(txtUserName)) valid = false
        if (Functions.validateTextView(txtUserLastName)) valid = false
        if (Functions.validateTextView(txtUserPhone)) valid = false
        if (Functions.validateTextView(txtUserEmail)) valid = false
        if (Functions.validateTextView(txtUserPassword)) valid = false

        if (type == "2") {
            if (Functions.validateTextView(txtUserBirthdate)) valid = false
            if (Functions.validateTextView(txtUserDocument, 8)) {
                return showNotification("Validación de Campos", "Ingrese un documento válido")
            }
            if (Functions.validateTextViewDate(txtUserBirthdate, Date(), 6570)) {
                return showNotification("Validación de Campos", "Debes ser mayor de edad")
            }
            if (!validDoc) {
                return showNotification(
                    "Validación de Documento",
                    "Debe validar su documento de identidad"
                )
            }
            if (imageUri == null) {
                return showNotification("Validación de Imagen", "Debe subir una foto de perfil")
            }
        }

        if (Functions.validateTextView(txtUserPhone, 9)) {
            return showNotification("Validación de Campos", "Ingrese un celular válido")
        }
        if (Functions.validateTextViewEmail(txtUserEmail)) {
            return showNotification("Validación de Campos", "Ingrese un email válido")
        }
        if (Functions.validateTextView(txtUserPassword, 6)) {
            return showNotification(
                "Validación de Campos",
                "La contraseña debe tener mínimo 6 caracteres"
            )
        }

        return valid
    }

    private fun showNotification(title: String, message: String): Boolean {
        alertDialog =
            showAlertDialog(requireContext(), R.layout.alert_dialog_1, title, message, null)
        return false;
    }

    private fun setUser(): User {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val user = User()
        user.names = txtUserName.text.toString().trim()
        user.lastNames = txtUserLastName.text.toString().trim()
        user.phone = txtUserPhone.text.toString()
        user.email = txtUserEmail.text.toString().trim()
        user.password = txtUserPassword.text.toString()

        if(type == "2") {
            user.document = txtUserDocument.text.toString()
            user.birthdate = dateFormat.parse(txtUserBirthdate.text.toString())
        }

        return user
    }

    private fun createAuthentication(user: User) {
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
                        createUser(user)
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

    private fun createUser(user: User) {
        viewModel.saveDB(user).observe(viewLifecycleOwner, Observer { result ->
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
                Glide.with(requireActivity()).load(photoFile).centerCrop().into(ivUserPhoto)
            }
            if (requestCode == IMAGE_PICK_CODE) {
                imageUri = data?.data!!
                Glide.with(requireActivity()).load(imageUri).centerCrop().into(ivUserPhoto)
            }
        }
    }

    inner class AsyncTaskHandler : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            alertDialog =
                Functions.createDialog(
                    requireActivity(),
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
                val cui = txtUserCui.text.toString()
                val cuiResult = jsonObject.getString("cui")

                if (cui == cuiResult) {
                    btnUserSearch.setColorFilter(Color.parseColor("#25F800"))
                    validDoc = true
                } else {
                    btnUserSearch.setColorFilter(Color.parseColor("#FF0000"))
                    validDoc = false
                }
            }

            if (alertDialog.isShowing) alertDialog.dismiss()
        }
    }
}