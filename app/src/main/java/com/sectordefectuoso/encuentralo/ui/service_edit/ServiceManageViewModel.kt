package com.sectordefectuoso.encuentralo.ui.service_edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sectordefectuoso.encuentralo.data.model.Service
import com.sectordefectuoso.encuentralo.domain.category.ICategoryUC
import com.sectordefectuoso.encuentralo.domain.service.IServiceUC
import com.sectordefectuoso.encuentralo.domain.storage.IStorageUC
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class ServiceManageViewModel(private val serviceUC: IServiceUC,
                             private val categoryUC: ICategoryUC,
                             private val storageUC: IStorageUC
) : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    val getCategories = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            categoryUC.getAll().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun getCategory(idSubCategory: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try{
            categoryUC.getCategoryBySubcategory(idSubCategory).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun getSubCategories(idCategory: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            categoryUC.getSubcategoriesByCategory(idCategory).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun uploadImage(uri: Uri, uid: String, path: String) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            storageUC.updateImage(uri, uid, path).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }

    fun saveDB(service: Service) = liveData(coroutineContext) {
        emit(ResourceState.Loading)
        try {
            if(service.documentId != "") {
                serviceUC.update(service).collect {
                    emit(it)
                }
            }
            else {
                serviceUC.create(service).collect {
                    emit(it)
                }
            }
        } catch (e: Exception) {
            emit(ResourceState.Failed(e.message!!))
        }
    }
}