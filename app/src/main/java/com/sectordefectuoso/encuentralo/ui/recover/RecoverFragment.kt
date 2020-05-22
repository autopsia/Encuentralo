package com.sectordefectuoso.encuentralo.ui.recover

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.utils.Functions
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_recover.*

class RecoverFragment : Fragment() {

    companion object {
        fun newInstance() = RecoverFragment()
    }

    private lateinit var viewModel: RecoverViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recover, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RecoverViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        btnRecoverSend.setOnClickListener {
            if(validate()){
                val email = txtRecoverEmail.text.trim().toString()
                auth.sendPasswordResetEmail(email).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Se envió un mensaje a su correo", Toast.LENGTH_SHORT).show()
                        nav_login_fragment.findNavController().popBackStack(R.id.loginFragment, false)
                    }
                    else{
                        Toast.makeText(context, Functions.getErrorAuthentication(task.exception), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun validate(): Boolean {
        var valid = true
        if(Functions.validateTextView(txtRecoverEmail)) valid = false

        return valid
    }
}
