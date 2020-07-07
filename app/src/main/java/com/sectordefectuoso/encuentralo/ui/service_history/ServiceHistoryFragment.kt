package com.sectordefectuoso.encuentralo.ui.service_history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.repository.chat.ChatRepo
import com.sectordefectuoso.encuentralo.data.repository.service.ServiceRepo
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.chat.ChatUC
import com.sectordefectuoso.encuentralo.domain.service.ServiceUC
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.ServiceHistoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_service_history.*

class ServiceHistoryFragment : BaseFragment() {

    companion object {
        fun newInstance() = ServiceHistoryFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this, ServiceHistoryViewModelFactory(
                UserUC(UserRepo()), ServiceUC(ServiceRepo()), ChatUC(ChatRepo())
            )
        ).get(ServiceHistoryViewModel::class.java)
    }

    override val TAG: String get() = "ServiceHistoryFragment"

    override fun getLayout(): Int = R.layout.fragment_service_history

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(getLayout(), container, false)
        val rvHistory: RecyclerView = root.findViewById(R.id.rvHistoryService)
        rvHistory.layoutManager = GridLayoutManager(this.context, 1, LinearLayoutManager.VERTICAL, false)
        getServiceIds()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun getServiceIds() {
        viewModel.getServiceIds().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    val services = result.data
                    val ids = services.map { it.documentId } as ArrayList
                    getUserIds(ids)
                    Log.d("SERVICE_IDS", ids.toString())
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_SERVICE_IDS", result.message)
                }
            }
        })
    }

    private fun getUserIds(serviceIds: ArrayList<String>){
        viewModel.getUserIds(serviceIds).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    val data = result.data
                    getData(data)
                    Log.d("USER_IDS", data.toString())
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_USER_IDS", result.message)
                }
            }
        })
    }

    private fun getData(map: ArrayList<Map<String, String>>) {
        viewModel.getData(map).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    val data = result.data
                    val adapter = ServiceHistoryAdapter(data)
                    rvHistoryService.adapter = adapter
                    Log.d("USER_SERVICE", data.toString())
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_USER_SERVICE", result.message)
                }
            }
        })
    }
}