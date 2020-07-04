package com.sectordefectuoso.encuentralo.ui.service_history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.data.repository.chat.ChatRepo
import com.sectordefectuoso.encuentralo.data.repository.user.UserRepo
import com.sectordefectuoso.encuentralo.domain.chat.ChatUC
import com.sectordefectuoso.encuentralo.domain.user.UserUC
import com.sectordefectuoso.encuentralo.ui.history.HistoryViewModel
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.sectordefectuoso.encuentralo.viewmodel.ServiceHistoryViewModelFactory

class ServiceHistoryFragment : BaseFragment() {

    companion object {
        fun newInstance() = ServiceHistoryFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this, ServiceHistoryViewModelFactory(
                UserUC(UserRepo()), ChatUC(ChatRepo())
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
        getIds()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun getIds(){
        viewModel.getUserIds("uNMCHEnLTCt1M7gcjdw9").observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {

                }
                is ResourceState.Success -> {
                    Log.d("IDS", result.data.toString())
                }
                is ResourceState.Failed -> {
                    Log.d("ERROR_IDS", result.message)
                }
            }
        })
    }
}