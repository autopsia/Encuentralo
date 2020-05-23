package com.sectordefectuoso.encuentralo.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.sectordefectuoso.encuentralo.R
import kotlinx.android.synthetic.main.alert_dialog_1.view.*
import java.lang.Exception

class Functions {
    companion object{
        fun validateTextView(textView: TextView): Boolean{
            val text = textView.text.trim()

            return if(text.isEmpty()){
                textView.setBackgroundResource(R.drawable.bg_textview_error_rounded)
                true
            } else{
                textView.setBackgroundResource(R.drawable.bg_textview_rounded)
                false
            }
        }
        fun getErrorAuthentication(exception: Exception?): String{
            return when(exception){
                is FirebaseAuthException -> {
                    when(exception.errorCode){
                        "ERROR_USER_NOT_FOUND" -> {
                            "El usuario ingresado no se encuentra registrado"
                        }
                        "ERROR_WRONG_PASSWORD" -> {
                            "La clave ingresada es incorrecta"
                        }
                        else -> {
                            exception.message.toString()
                        }
                    }
                }
                is FirebaseException -> {
                    if(exception.message.toString().contains("blocked all requests")){
                        "La solicitud se ha bloqueado por varios intentos fallidos. Inténtelo más tarde"
                    }
                    else {
                        "Error: " + exception!!.message.toString()
                    }
                }
                else -> {
                    exception!!.message.toString()
                }
            }
        }
        fun createDialog(contex: Context, layout: Int, title: String, message: String, callbackOk: (() -> Unit)?) {
            val dialog = LayoutInflater.from(contex).inflate(layout, null)
            val builder = AlertDialog.Builder(contex).setView(dialog)
            val alert = builder.show()
            alert.setCanceledOnTouchOutside(false)
            alert.window?.setBackgroundDrawable(ColorDrawable(0))

            when(layout){
                R.layout.alert_dialog_1 ->{
                    dialog.lblDialog1Title.setText(title)
                    dialog.lblDialog1Message.setText(message)
                    dialog.btnDialog1Ok.setOnClickListener {
                        if(callbackOk != null){
                            callbackOk()
                        }
                        alert.dismiss()
                    }
                }
                else -> {
                }
            }
        }
    }
}