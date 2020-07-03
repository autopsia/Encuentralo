package com.sectordefectuoso.encuentralo.ui.service

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.repository.service.ServiceRepo
import com.sectordefectuoso.encuentralo.domain.service.ServiceUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.ServiceViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_service.*
import kotlinx.android.synthetic.main.fragment_service.*

class ServiceFragment : BaseFragment() {

    companion object {
        fun newInstance() = ServiceFragment()
    }

    private val viewModel by lazy { ViewModelProvider(this, ServiceViewModelFactory(ServiceUC(ServiceRepo()))).get(ServiceViewModel::class.java) }

    override val TAG: String
        get() = "ServiceFragment"

    override fun getLayout(): Int = R.layout.fragment_service

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(getLayout(), container, false)
        val rvServices: RecyclerView = root.findViewById(R.id.rvServices)
        rvServices.layoutManager = GridLayoutManager(this.context, 1, LinearLayoutManager.VERTICAL, false)
        observeData()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnCreateService.setOnClickListener {
            nav_host_service.findNavController().navigate(
                R.id.action_navigation_service_to_serviceManageFragment
            )
        }
    }

    private fun observeData(){
        viewModel.getServices.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i("SERVICIOS ", "Cargando")
                }
                is ResourceState.Success -> {
                    val services = result.data
                    val adapter = ServiceAdapter(services as ArrayList<Service>)
                    rvServices.adapter = adapter
                }
                is ResourceState.Failed -> {
                    Log.i("ERROR_SERVICIO:", result.message)

                }
            }
        })
    }
}