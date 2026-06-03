package com.alvaro.projectpenjualan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Sembunyikan action bar jika ada
        supportActionBar?.hide()

        // Delay selama 2-3 detik untuk branding
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserSession()
        }, 2500)
    }

    private fun checkUserSession() {
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Jika sudah login, langsung ke Dashboard Utama
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Jika belum login, ke halaman Login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}