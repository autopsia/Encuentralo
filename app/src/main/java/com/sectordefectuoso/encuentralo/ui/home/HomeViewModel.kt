package com.sectordefectuoso.encuentralo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sectordefectuoso.encuentralo.data.model.Category

class HomeViewModel : ViewModel() {
    private val _categories = MutableLiveData<Category>().apply {
        value = Category("oscar", 1)
    }
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    val category: LiveData<Category> = _categories
}