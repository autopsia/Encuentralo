package com.sectordefectuoso.encuentralo.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sectordefectuoso.encuentralo.MainActivity

import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.utils.Functions
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            if(validate()){
                alertDialog = Functions.createDialog(requireContext(), R.layout.alert_dialog_2, "Iniciando sesión", "", null)!!
                val email = txtLoginEmail.text.trim().toString()
                val password = txtLoginPassword.text.toString()
                login(email, password)
            }
        }
        lblLoginRecover.setOnClickListener{
            nav_login_fragment.findNavController().navigate(R.id.action_loginFragment_to_recoverFragment)
        }
        lblLoginSignIn.setOnClickListener{
            var bundle = Bundle()
            val fun1 = {
                bundle = bundleOf("type" to "1")
                nav_login_fragment.findNavController().navigate(R.id.action_loginFragment_to_registerUserFragment, bundle)
            }
            val fun2 = {
                bundle = bundleOf("type" to "2")
                nav_login_fragment.findNavController().navigate(R.id.action_loginFragment_to_registerUserFragment, bundle)
            }
            Functions.createDialog2(requireContext(), "¿Deseas registrarte como?", "", "Cliente", "Ofertante", fun1, fun2)
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(){ task ->
            if (task.isSuccessful) {
                alertDialog.dismiss()
                Thread.sleep(1000)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            else {
                Functions.showAlert(requireContext(), alertDialog, "Atención", Functions.getErrorAuthentication(task.exception))
            }
        }
    }
    fun validate(): Boolean {
        var valid = true
        if(Functions.validateTextView(txtLoginEmail)) valid = false
        if(Functions.validateTextView(txtLoginPassword)) valid = false

        return valid
    }
}
