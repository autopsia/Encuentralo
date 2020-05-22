package com.sectordefectuoso.encuentralo.ui.login

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.R.drawable
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            if(validate()){
                val email = txtLoginEmail.text.trim().toString()
                val password = txtLoginPassword.text.trim().toString()
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(){ task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Ingresó correctamente", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "Fallo en la autenticación", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        lblLoginRecover.setOnClickListener{
            nav_login_fragment.findNavController().navigate(R.id.action_loginFragment_to_recoverFragment)
        }
        lblLoginSignIn.setOnClickListener{
            nav_login_fragment.findNavController().navigate(R.id.action_loginFragment_to_registerUserFragment)
        }
    }

    fun validate(): Boolean {
        var valid = true
        if(validateTextView(txtLoginEmail)) valid = false
        if(validateTextView(txtLoginPassword)) valid = false

        return valid
    }

    fun validateTextView(textView: TextView): Boolean{
        val text = textView.text.trim()

        return if(text.isEmpty()){
            textView.setBackgroundResource(drawable.bg_textview_error_rounded)
            true
        } else{
            textView.setBackgroundResource(drawable.bg_textview_rounded)
            false
        }
    }
}
