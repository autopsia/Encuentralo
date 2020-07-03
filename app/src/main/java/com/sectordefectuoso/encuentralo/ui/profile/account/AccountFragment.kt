package com.sectordefectuoso.encuentralo.ui.profile.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.LoginActivity
import com.sectordefectuoso.encuentralo.MainActivity
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.ServiceActivity
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.data.repository.storage.StorageRepo
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.storage.StorageUC
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.Functions
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.AccountViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account.*
import java.text.SimpleDateFormat

class AccountFragment : BaseFragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            AccountViewModelFactory(UserUC(UserRepo()), StorageUC(StorageRepo()))
        ).get(AccountViewModel::class.java)
    }
    private var user: User? = null

    override val TAG: String get() = "AccountFragment"

    override fun getLayout(): Int = R.layout.fragment_account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(getLayout(), container, false)
        setInformation()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnAccountEdit.setOnClickListener {
            var bundle = bundleOf("user" to Gson().toJson(user))
            nav_host_fragment.findNavController()
                .navigate(R.id.action_navigation_account_to_navigation_profile, bundle)
        }
        btnAccountLogout.setOnClickListener {
            logout()
        }
        fabService.setOnClickListener {
            var callbackOk: (() -> Unit)? = {
                val intent = Intent(requireContext(), ServiceActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            val title = "¿Desea ingresar al módulo de servicio?"
            val message = "Aqui podrá administrar los servicios que ofrece"
            Functions.createDialog(requireContext(), R.layout.alert_dialog_3, title, message, callbackOk)
        }
    }

    private fun setInformation() {
        viewModel.getUser().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    user = result.data
                    lblAccountDocument.text = user?.document
                    lblAccountName.text = "${user?.names} ${user?.lastNames}"
                    lblAccountBirthdate.text =
                        SimpleDateFormat("dd/MM/yyyy").format(user?.birthdate)
                    lblAccountPhone.text = user?.phone
                    lblAccountEmail.text = user?.email
                    loadImage(user!!.documentId)
                }
                is ResourceState.Failed -> {
                    Functions.showAlert(requireContext(), null, "Alerta", result.message)
                }
            }
        })
    }

    private fun loadImage(uid: String) {
        viewModel.loadImage("User/$uid.jpg")
            .observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is ResourceState.Loading -> {
                        Log.d("LOADING_PHOTO", "Cargando imagen")
                    }
                    is ResourceState.Success -> {
                        val url = result.data
                        Glide.with(requireContext()).load(url)
                            .centerCrop().into(ivAccountPhoto)
                    }
                    is ResourceState.Failed -> {
                        Log.d("ERROR_PHOTO", result.message)
                    }
                }
            })
    }

    private fun logout() {
        viewModel.logout().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    if (result.data) {
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
                is ResourceState.Failed -> {

                }
            }
        })
    }
}