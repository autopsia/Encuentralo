package com.sectordefectuoso.encuentralo.ui.register.user

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sectordefectuoso.encuentralo.MainActivity

import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.Functions
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_register_user.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class RegisterUserFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterUserFragment()
    }

    private lateinit var viewModel: RegisterUserViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterUserViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                var user = User()
                user.document = txtUserDocument.text.toString()
                user.names = txtUserName.text.toString().trim()
                user.lastNames = txtUserLastName.text.toString().trim()
                user.birthdate = dateFormat.parse(txtUserBirthdate.text.toString())
                user.phone = txtUserPhone.text.toString()
                user.email = txtUserEmail.text.toString().trim()
                val password: String = txtUserPassword.text.toString()

                if(chbUserService.isChecked){
                    var bundle = bundleOf("user" to user.toString(), "password" to password)
                    nav_login_fragment.findNavController().navigate(R.id.action_registerUserFragment_to_registerServiceFragment, bundle)
                }
                else{
                    alertDialog = Functions.createDialog(requireContext(), R.layout.alert_dialog_2, "Guardando", "", null)!!
                    createAuthentication(user, password)
                }
            }
        }
    }

    fun validate(): Boolean {
        var valid = true
        if (Functions.validateTextView(txtUserDocument)) valid = false
        if (Functions.validateTextView(txtUserName)) valid = false
        if (Functions.validateTextView(txtUserLastName)) valid = false
        if (Functions.validateTextView(txtUserBirthdate)) valid = false
        if (Functions.validateTextView(txtUserPhone)) valid = false
        if (Functions.validateTextView(txtUserEmail)) valid = false
        if (Functions.validateTextView(txtUserPassword)) valid = false

        if (Functions.validateTextView(txtUserDocument, 8)) return showNotification("Validación de Campos", "Ingrese un documento válido")
        if (Functions.validateTextViewDate(txtUserBirthdate, Date(),6570)) return showNotification("Validación de Campos", "Debes ser mayor de edad")
        if (Functions.validateTextView(txtUserPhone, 9)) return showNotification("Validación de Campos", "Ingrese un celular válido")
        if (Functions.validateTextViewEmail(txtUserEmail)) return showNotification("Validación de Campos", "Ingrese un email válido")
        if (Functions.validateTextView(txtUserPassword, 6)) return showNotification("Validación de Campos", "La contraseña debe tener mínimo 6 caracteres")
        return valid
    }

    private fun showNotification(title: String, message: String): Boolean {
        alertDialog =
            Functions.createDialog(
                requireActivity(),
                R.layout.alert_dialog_1,
                title,
                message,
                null
            )!!
        return false;
    }

    private fun createAuthentication(user: User, password: String){
        auth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                task.result?.user?.uid?.let { createUser(it, user) }
            }
            else{
                Functions.showAlert(requireActivity(), alertDialog, "Atención", Functions.AUTH_FAIL)
            }
        }
    }

    private fun createUser(key: String, user: User){
        val userMap = hashMapOf(
            "document" to user.document,
            "names" to user.names,
            "lastNames" to user.lastNames,
            "birthdate" to user.birthdate,
            "phone" to user.phone,
            "email" to user.email
        )

        db.collection("users").document(key).set(userMap).addOnCompleteListener { task ->
            if(task.isSuccessful){
                alertDialog.dismiss()
                Thread.sleep(1500)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            else{
                Functions.showAlert(requireActivity(), alertDialog, "Atención", Functions.DB_FAIL)
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
