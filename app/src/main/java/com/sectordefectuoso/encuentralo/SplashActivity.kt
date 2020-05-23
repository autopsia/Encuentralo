package com.sectordefectuoso.encuentralo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        Thread.sleep(2000)
        var intent :Intent
        intent = if (auth.currentUser != null) {
            Intent(applicationContext, MainActivity::class.java)
        } else{
            Intent(applicationContext, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
