package com.sectordefectuoso.encuentralo.ui.profile.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.sectordefectuoso.encuentralo.LoginActivity
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.AccountViewModelFactory
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
            AccountViewModelFactory(UserUC(UserRepo()))
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
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_ACCOUNT", result.message)
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
                    if (result.data){
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