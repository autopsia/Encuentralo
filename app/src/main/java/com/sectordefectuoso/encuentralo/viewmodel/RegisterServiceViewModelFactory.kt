package com.sectordefectuoso.encuentralo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sectordefectuoso.encuentralo.domain.IUseCase
import com.sectordefectuoso.encuentralo.domain.category.ICategoryUC
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC
import com.sectordefectuoso.encuentralo.domain.user.IUserUC

class RegisterServiceViewModelFactory(private val serviceUC: IServiceUC, private val categoryUC: ICategoryUC, private val userUC: IUserUC): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(IServiceUC::class.java, ICategoryUC::class.java, IUserUC::class.java).newInstance(serviceUC, categoryUC, userUC)
    }
}