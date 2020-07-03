package com.sectordefectuoso.encuentralo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sectordefectuoso.encuentralo.domain.user.IUserUC

class RegisterUserViewModelFactory(private val userCase: IUserUC): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(IUserUC::class.java).newInstance(userCase)
    }
}