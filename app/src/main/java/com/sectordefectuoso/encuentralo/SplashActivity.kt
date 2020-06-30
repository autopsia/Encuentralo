package com.sectordefectuoso.encuentralo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build

class SplashActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST = 10
    private var permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        askPermission()
    }

    private fun openActivity() {
        Thread.sleep(1000)
        var intent: Intent = if (auth.currentUser != null) {
            Intent(applicationContext, MainActivity::class.java)
        } else {
            Intent(applicationContext, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }

    private fun askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions(permissions)) {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
            else{
                openActivity()
            }
        }
        else{
            openActivity()
        }
    }

    private fun checkPermissions(permissionArray: Array<String>): Boolean {
        var result = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED) {
                result = false
            }
        }

        return result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST) {
            var result = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    result = false
                }
            }

            if (!result) {
                askPermission()
            }
            else{
                openActivity()
            }
        }
    }
}
