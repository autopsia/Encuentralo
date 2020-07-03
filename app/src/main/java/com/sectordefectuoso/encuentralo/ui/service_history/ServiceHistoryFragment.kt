package com.sectordefectuoso.encuentralo.ui.service_history

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sectordefectuoso.encuentralo.R

class ServiceHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = ServiceHistoryFragment()
    }

    private lateinit var viewModel: ServiceHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ServiceHistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}