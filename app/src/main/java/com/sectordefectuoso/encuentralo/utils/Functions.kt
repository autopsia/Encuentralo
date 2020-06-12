package com.sectordefectuoso.encuentralo.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.sectordefectuoso.encuentralo.R
import io.grpc.KnownLength
import kotlinx.android.synthetic.main.alert_dialog_1.view.*
import kotlinx.android.synthetic.main.alert_dialog_2.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


class Functions {
    companion object {
        const val DB_FAIL = "Fallo en el registro de la base de datos"
        const val AUTH_FAIL = "Fallo en el registro de autenticación"

        fun validateTextView(textView: TextView): Boolean {
            val text = textView.text.trim()

            return if (text.isEmpty()) {
                textView.setBackgroundResource(R.drawable.bg_textview_error_rounded)
                true
            } else {
                textView.setBackgroundResource(R.drawable.bg_textview_rounded)
                false
            }
        }

        fun validateTextView(textView: TextView, length: Int): Boolean {
            val text = textView.text.trim()

            return if (text.isEmpty() || text.length < length) {
                textView.setBackgroundResource(R.drawable.bg_textview_error_rounded)
                true
            } else {
                textView.setBackgroundResource(R.drawable.bg_textview_rounded)
                false
            }
        }

        fun validateTextViewEmail(textView: TextView): Boolean {
            val text = textView.text.trim()
            val emailExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

            val pattern: Pattern = Pattern.compile(emailExpression, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(text)

            return if (text.isEmpty() || !matcher.matches()) {
                textView.setBackgroundResource(R.drawable.bg_textview_error_rounded)
                true
            } else {
                textView.setBackgroundResource(R.drawable.bg_textview_rounded)
                false
            }
        }

        fun validateTextViewDate(textView: TextView, currentDate: Date, days: Int): Boolean {
            val text = textView.text.trim()

            return if (text.isEmpty()) {
                textView.setBackgroundResource(R.drawable.bg_textview_error_rounded)
                true
            } else {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val secondDate: Date = sdf.parse(text.toString())

                val diffInMillies = Math.abs(secondDate.time - currentDate.time)
                val diff: Long = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
                return if (diff < days) {
                    textView.setBackgroundResource(R.drawable.bg_textview_error_rounded)
                    true
                } else {
                    textView.setBackgroundResource(R.drawable.bg_textview_rounded)
                    false
                }
            }
        }

        fun getErrorAuthentication(exception: Exception?): String {
            return when (exception) {
                is FirebaseAuthException -> {
                    when (exception.errorCode) {
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
                    if (exception.message.toString().contains("blocked all requests")) {
                        "La solicitud se ha bloqueado por varios intentos fallidos. Inténtelo más tarde"
                    } else {
                        "Error: " + exception!!.message.toString()
                    }
                }
                else -> {
                    exception!!.message.toString()
                }
            }
        }

        fun createDialog(contex: Context, layout: Int, title: String, message: String, callbackOk: (() -> Unit)?): AlertDialog? {
            val dialog = LayoutInflater.from(contex).inflate(layout, null)
            val builder = AlertDialog.Builder(contex).setView(dialog)
            val alert = builder.show()
            alert.setCanceledOnTouchOutside(false)

            when (layout) {
                R.layout.alert_dialog_1 -> {
                    alert.window?.setBackgroundDrawable(ColorDrawable(0))
                    dialog.lblDialog1Title.setText(title)
                    dialog.lblDialog1Message.setText(message)
                    dialog.btnDialog1Ok.setOnClickListener {
                        if (callbackOk != null) {
                            callbackOk()
                        }
                        alert.dismiss()
                    }
                }
                else -> {
                    val rotate = RotateAnimation(
                        0f, 360f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    rotate.duration = 1000
                    rotate.repeatCount = Animation.INFINITE
                    dialog.lblDialog2Title.setText(title)
                    dialog.ivDialog2Image.startAnimation(rotate)
                }
            }

            return alert
        }

        fun closeKeyBoard(contex: Context, view: View) {
            if (view != null) {
                val imm: InputMethodManager =
                    contex.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun openCalendar(contex: Context, textView: TextView) {
            val calendar = Calendar.getInstance()
            var day: Int =
                if (textView.text.isEmpty()) calendar.get(Calendar.DAY_OF_MONTH) else Integer.parseInt(
                    textView.text.substring(0, 2)
                )
            var month: Int =
                if (textView.text.isEmpty()) calendar.get(Calendar.MONTH) else Integer.parseInt(
                    textView.text.substring(3, 5)
                ).minus(1)
            var year: Int =
                if (textView.text.isEmpty()) calendar.get(Calendar.YEAR) else Integer.parseInt(
                    textView.text.substring(6, 10)
                )

            val dpd = DatePickerDialog(
                contex,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val strMonth =
                        if (monthOfYear.toString().length == 1) "0${monthOfYear + 1}" else "$monthOfYear"
                    val strDay =
                        if (dayOfMonth.toString().length == 1) "0${dayOfMonth}" else "$dayOfMonth"
                    textView.text = "$strDay/$strMonth/$year"
                },
                year,
                month,
                day
            )

            dpd.show()
        }
    }
}