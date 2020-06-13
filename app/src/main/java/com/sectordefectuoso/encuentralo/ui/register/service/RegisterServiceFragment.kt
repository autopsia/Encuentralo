package com.sectordefectuoso.encuentralo.ui.register.service

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sectordefectuoso.encuentralo.R
import kotlinx.android.synthetic.main.fragment_register_service.*
import kotlinx.android.synthetic.main.fragment_register_user.*

class RegisterServiceFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterServiceFragment()
    }

    private lateinit var viewModel: RegisterServiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_service, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterServiceViewModel::class.java)
        // TODO: Use the ViewModel

    }

}