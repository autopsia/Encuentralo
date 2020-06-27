package com.sectordefectuoso.encuentralo.ui.recover

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.Functions
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.RecoverViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_recover.*
import kotlin.concurrent.thread

class RecoverFragment : BaseFragment() {

    companion object {
        fun newInstance() = RecoverFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            RecoverViewModelFactory(UserUC(UserRepo()))
        ).get(RecoverViewModel::class.java)
    }
    private lateinit var alertDialog: AlertDialog

    override val TAG: String
        get() = "RecoverFragment"

    override fun getLayout(): Int = R.layout.fragment_recover

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnRecoverSend.setOnClickListener {
            Functions.closeKeyBoard(requireActivity(), requireView())
            if(validate()){
                val email = txtRecoverEmail.text.trim().toString()
                sendEmail(email)
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        if(Functions.validateTextView(txtRecoverEmail)) valid = false

        return valid
    }

    private fun sendEmail(email: String) {
        var title = "Recuperación"
        var message = "Se envió el mensaje a su correo"
        var callbackOk: (() -> Unit)? = null

        viewModel.updatePassword(email).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    alertDialog = showAlertDialog(requireContext(), R.layout.alert_dialog_2, "Enviando mensaje", "", null)
                }
                is ResourceState.Success -> {
                    alertDialog.dismiss()
                    callbackOk = {
                        nav_login_fragment.findNavController().popBackStack(R.id.loginFragment, false)
                    }
                    Functions.createDialog(requireContext(), R.layout.alert_dialog_1, title, message, callbackOk)
                }
                is ResourceState.Failed -> {
                    alertDialog.dismiss()
                    title = "Alerta"
                    message = result.message
                    Functions.createDialog(requireContext(), R.layout.alert_dialog_1, title, message, callbackOk)
                }
            }
        })
    }
}