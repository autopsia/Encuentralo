package com.sectordefectuoso.encuentralo.ui.profile.edit

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.User
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sectordefectuoso.encuentralo.utils.Functions
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val profileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java)}
    private lateinit var alertDialog: AlertDialog
    private var userBundle: User = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadInformation()

        txtAccountBirthdate.setOnClickListener{
            Functions.openCalendar(requireContext(), txtAccountBirthdate)
        }

        btnAccountSave.setOnClickListener {
            if(validate()){
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                var user = User()
                user.document = txtAccountDocument.text.toString()
                user.names = txtAccountName.text.toString()
                user.lastNames = txtAccountLastName.text.toString()
                user.birthdate = dateFormat.parse(txtAccountBirthdate.text.toString())
                user.phone = txtAccountPhone.text.toString()
                user.email = txtAccountEmail.text.toString()
                profileViewModel.user = user

                if(user.email.equals(userBundle.email)){
                    updateInformation()
                }
                else{
                    updateEmail()
                }
            }
        }
    }

    private fun loadInformation(){
        userBundle = arguments?.get("user") as User
        txtAccountDocument.setText(userBundle.document)
        txtAccountName.setText(userBundle.names)
        txtAccountLastName.setText(userBundle.lastNames)
        txtAccountBirthdate.setText(SimpleDateFormat("dd/MM/yyyy").format(userBundle.birthdate))
        txtAccountPhone.setText(userBundle.phone)
        txtAccountEmail.setText(userBundle.email)
    }

    private fun validate(): Boolean{
        var valid = true
        if (Functions.validateTextView(txtAccountDocument)) valid = false
        if (Functions.validateTextView(txtAccountName)) valid = false
        if (Functions.validateTextView(txtAccountLastName)) valid = false
        if (Functions.validateTextView(txtAccountBirthdate)) valid = false
        if (Functions.validateTextView(txtAccountPhone)) valid = false
        if (Functions.validateTextView(txtAccountEmail)) valid = false

        if (Functions.validateTextView(txtAccountDocument, 8)) return showNotification("Validación de Campos", "Ingrese un documento válido")
        if (Functions.validateTextViewDate(txtAccountBirthdate, Date(),6570)) return showNotification("Validación de Campos", "Debes ser mayor de edad")
        if (Functions.validateTextView(txtAccountPhone, 9)) return showNotification("Validación de Campos", "Ingrese un celular válido")
        if (Functions.validateTextViewEmail(txtAccountEmail)) return showNotification("Validación de Campos", "Ingrese un email válido")
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

    private fun updateEmail() {
        profileViewModel.updateEmail.observe(viewLifecycleOwner, Observer{ result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i("observable ", "Cargando")
                }
                is ResourceState.Success -> {
                    updateInformation()
                }
                is ResourceState.Failed -> {
                    Log.i("failure", "error")

                }
            }
        })
    }

    private fun updateInformation() {
        profileViewModel.updateInformation.observe(viewLifecycleOwner, Observer{ result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i("observable ", "Cargando")
                }
                is ResourceState.Success -> {
                    nav_host_fragment.findNavController().popBackStack(R.id.navigation_account, false)
                }
                is ResourceState.Failed -> {
                    Log.i("failure", "error")

                }
            }
        })
    }
}