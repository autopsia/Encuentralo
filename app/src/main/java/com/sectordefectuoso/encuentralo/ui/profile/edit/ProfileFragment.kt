package com.sectordefectuoso.encuentralo.ui.profile.edit

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.Functions
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_register_user.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        loadInformation()

        txtAccountBirthdate.setOnClickListener{
            Functions.openCalendar(requireContext(), txtAccountBirthdate)
        }

        btnAccountSave.setOnClickListener {
            if(validate()){

            }
        }
    }

    private fun loadInformation(){
        var user = arguments?.get("user") as User
        txtAccountDocument.setText(user.document)
        txtAccountName.setText(user.names)
        txtAccountLastName.setText(user.lastNames)
        txtAccountBirthdate.setText(SimpleDateFormat("dd/MM/yyyy").format(user.birthdate))
        txtAccountPhone.setText(user.phone)
        txtAccountEmail.setText(user.email)
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

    
}