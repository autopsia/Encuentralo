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
import com.google.firebase.auth.FirebaseAuth
import com.sectordefectuoso.encuentralo.LoginActivity
import com.sectordefectuoso.encuentralo.MainActivity
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account.*
import java.io.Serializable
import java.text.SimpleDateFormat

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private val accountViewModel by lazy {ViewModelProvider(this).get(AccountViewModel::class.java)}
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_account, container, false)
        loadInformation()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnAccountEdit.setOnClickListener {
            var bundle = bundleOf("user" to user as Serializable)
            nav_host_fragment.findNavController().navigate(R.id.action_navigation_account_to_navigation_profile, bundle)
        }

        btnAccountLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    fun loadInformation(){
        accountViewModel.getUser.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i("observable ", "Cargando")
                }
                is ResourceState.Success -> {
                    user = result.data
                    lblAccountDocument.text = user?.document
                    lblAccountName.text = "${user?.names} ${user?.lastNames}"
                    lblAccountBirthdate.text = SimpleDateFormat("dd/MM/yyyy").format(user?.birthdate)
                    lblAccountPhone.text = user?.phone
                    lblAccountEmail.text = user?.email

                    Log.i("success", "${result.data}")
                }
                is ResourceState.Failed -> {
                    Log.i("failure", "error")

                }
            }
        })
    }
}