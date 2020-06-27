package com.sectordefectuoso.encuentralo.ui.profile.edit

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.User
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.Functions
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.ProfileViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ProfileViewModelFactory(UserUC(UserRepo()))
        ).get(ProfileViewModel::class.java)
    }
    private lateinit var alertDialog: AlertDialog
    private var userBundle: User = User()

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
                if (user.email.equals(userBundle.email)) {
                    updateUser(user)
                } else {
                    updateEmail(user)
                }
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
        txtAccountEmail.setText(userBundle.email)
    }

    private fun validate(): Boolean {
        var valid = true
        if (Functions.validateTextView(txtAccountDocument)) valid = false
        if (Functions.validateTextView(txtAccountName)) valid = false
        if (Functions.validateTextView(txtAccountLastName)) valid = false
        if (Functions.validateTextView(txtAccountBirthdate)) valid = false
        if (Functions.validateTextView(txtAccountPhone)) valid = false
        if (Functions.validateTextView(txtAccountEmail)) valid = false

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
        if (Functions.validateTextViewEmail(txtAccountEmail)) return showNotification(
            "Validación de Campos",
            "Ingrese un email válido"
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
        var user = User()
        user.document = txtAccountDocument.text.toString()
        user.names = txtAccountName.text.toString()
        user.lastNames = txtAccountLastName.text.toString()
        user.birthdate = dateFormat.parse(txtAccountBirthdate.text.toString())
        user.phone = txtAccountPhone.text.toString()
        user.email = txtAccountEmail.text.toString()

        return user
    }

    private fun updateEmail(user: User) {
        viewModel.updateEmail(user.email, userBundle.email, userBundle.password).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    updateUser(user)
                }
                is ResourceState.Failed -> {
                    hideAlertDialog(alertDialog)
                    Log.d("ERROR_PROFILE", result.message)
                }
            }
        })
    }

    private fun updateUser(user: User) {
        viewModel.updateDB(user).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    hideAlertDialog(alertDialog)
                    nav_host_fragment.findNavController()
                        .popBackStack(R.id.navigation_account, false)
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_PROFILE", result.message)
                }
            }
        })
    }
}