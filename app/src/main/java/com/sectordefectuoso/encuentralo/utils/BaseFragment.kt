package com.sectordefectuoso.encuentralo.utils

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected abstract val TAG : String
    protected abstract fun getLayout() : Int
}