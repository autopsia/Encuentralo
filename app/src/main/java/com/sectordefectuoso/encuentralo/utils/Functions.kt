package com.sectordefectuoso.encuentralo.utils

import android.widget.TextView
import com.google.firebase.auth.FirebaseAuthException
import com.sectordefectuoso.encuentralo.R
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
                else -> {
                    exception!!.message.toString()
                }
            }
        }
    }
}