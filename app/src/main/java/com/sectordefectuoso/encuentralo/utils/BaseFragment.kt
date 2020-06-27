package com.sectordefectuoso.encuentralo.utils

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected abstract val TAG: String
    protected abstract fun getLayout(): Int

    fun showAlertDialog(context: Context, layout: Int, title: String, message: String, callbackOk: (() -> Unit)?): AlertDialog {
        return Functions.createDialog(
            context,
            layout,
            title,
            message,
            callbackOk
        )!!
    }

    fun hideAlertDialog(alertDialog: AlertDialog){
        alertDialog.dismiss()
    }
}