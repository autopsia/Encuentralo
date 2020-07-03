package com.sectordefectuoso.encuentralo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC

class ServiceViewModelFactory(private val serviceCase: IServiceUC): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(IServiceUC::class.java).newInstance(serviceCase)
    }
}