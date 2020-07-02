package com.sectordefectuoso.encuentralo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sectordefectuoso.encuentralo.domain.storage.IStorageUC
import com.sectordefectuoso.encuentralo.domain.user.IUserUC

class AccountViewModelFactory(private val userCase: IUserUC, private val storageCase: IStorageUC): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(IUserUC::class.java, IStorageUC::class.java).newInstance(userCase, storageCase)
    }
}