package com.sectordefectuoso.encuentralo.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.data.repository.chat.ChatRepo
import com.sectordefectuoso.encuentralo.data.repository.service.ServiceRepo
import com.sectordefectuoso.encuentralo.data.repository.storage.StorageRepo
import com.sectordefectuoso.encuentralo.domain.chat.ChatUC
import com.sectordefectuoso.encuentralo.domain.service.ServiceUC
import com.sectordefectuoso.encuentralo.domain.storage.StorageUC
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.HistoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : BaseFragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this, HistoryViewModelFactory(
                ServiceUC(ServiceRepo()), ChatUC(ChatRepo()))
        ).get(HistoryViewModel::class.java)
    }

    override val TAG: String get() = "HistoryFragment"

    override fun getLayout(): Int = R.layout.fragment_history

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(getLayout(), container, false)
        val rvHistory: RecyclerView = root.findViewById(R.id.rvHistory)
        rvHistory.layoutManager = GridLayoutManager(this.context, 1, LinearLayoutManager.VERTICAL, false)
        getIds()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    private fun getIds(){
        viewModel.serviceIds.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    Log.d("IDS", result.data.toString())
                    getServices(result.data)
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_IDS", result.message)
                }
            }
        })
    }

    private fun getServices(ids: List<String>) {
        viewModel.getServices(ids).observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    Log.d("SERVICES", result.data.toString())
                    val services = result.data
                    val adapter = HistoryAdapter(services as ArrayList<Service>)
                    rvHistory.adapter = adapter
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_SERVICES", result.message)
                }
            }
        })
    }
}