package com.sectordefectuoso.encuentralo.ui.register.user

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class RegisterUserFragment : BaseFragment() {

    companion object {
        fun newInstance() = RegisterUserFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            RegisterUserViewModelFactory(UserUC(UserRepo()))
        ).get(RegisterUserViewModel::class.java)
    }
    private lateinit var alertDialog: AlertDialog

    override val TAG: String get() = "RegisterUserFragment"

    override fun getLayout(): Int = R.layout.fragment_register_user

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chbUserService.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btnUserRegister.setText("Siguiente")
            } else {
                btnUserRegister.setText("Registrar")
            }
        }
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
        btnUserRegister.setOnClickListener {
            if (validate()) {
                var user = setUser()

                if (chbUserService.isChecked) {
                    var bundle = bundleOf("user" to Gson().toJson(user))
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

    private fun validate(): Boolean {
        var valid = true
        if (Functions.validateTextView(txtUserDocument)) valid = false
        if (Functions.validateTextView(txtUserName)) valid = false
        if (Functions.validateTextView(txtUserLastName)) valid = false
        if (Functions.validateTextView(txtUserBirthdate)) valid = false
        if (Functions.validateTextView(txtUserPhone)) valid = false
        if (Functions.validateTextView(txtUserEmail)) valid = false
        if (Functions.validateTextView(txtUserPassword)) valid = false

        if (Functions.validateTextView(
                txtUserDocument,
                8
            )
        ) return showNotification("Validación de Campos", "Ingrese un documento válido")
        if (Functions.validateTextViewDate(txtUserBirthdate, Date(), 6570)) return showNotification(
            "Validación de Campos",
            "Debes ser mayor de edad"
        )
        if (Functions.validateTextView(
                txtUserPhone,
                9
            )
        ) return showNotification("Validación de Campos", "Ingrese un celular válido")
        if (Functions.validateTextViewEmail(txtUserEmail)) return showNotification(
            "Validación de Campos",
            "Ingrese un email válido"
        )
        if (Functions.validateTextView(
                txtUserPassword,
                6
            )
        ) return showNotification(
            "Validación de Campos",
            "La contraseña debe tener mínimo 6 caracteres"
        )
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
        user.document = txtUserDocument.text.toString()
        user.names = txtUserName.text.toString().trim()
        user.lastNames = txtUserLastName.text.toString().trim()
        user.birthdate = dateFormat.parse(txtUserBirthdate.text.toString())
        user.phone = txtUserPhone.text.toString()
        user.email = txtUserEmail.text.toString().trim()
        user.password = txtUserPassword.text.toString()

        return user
    }

    private fun createAuthentication(user: User) {
        viewModel.saveAuth(user.email, user.password).observe(viewLifecycleOwner, Observer { result ->
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
                    Thread.sleep(1500)
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

    inner class AsyncTaskHandler : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            alertDialog =
                Functions.createDialog(
                    requireActivity(),
                    R.layout.alert_dialog_2,
                    "Buscando en RENIEC",
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

            val jsonObject = JSONObject(result)
            txtUserName.setText(jsonObject.getString("nombres"))
            txtUserLastName.setText(
                jsonObject.getString("apellido_paterno") + " " + jsonObject.getString(
                    "apellido_materno"
                )
            )
            if (alertDialog.isShowing) alertDialog.dismiss()
        }
    }
}