package com.sectordefectuoso.encuentralo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sectordefectuoso.encuentralo.domain.chat.IChatUC
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC
import com.sectordefectuoso.encuentralo.domain.user.IUserUC

class ServiceHistoryViewModelFactory(private val userUC: IUserUC, private val serviceUC: IServiceUC, private val chatUC: IChatUC): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(IUserUC::class.java, IServiceUC::class.java, IChatUC::class.java).newInstance(userUC, serviceUC, chatUC)
    }
}