package com.alvaro.projectpenjualan.akun

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.R

class DataAkun : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pastikan nama R.layout ini sesuai dengan nama file XML kamu
        setContentView(R.layout.activity_data_akun)

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            // Cukup tampilkan notifikasi saat tombol diklik
            Toast.makeText(this, "Fitur Logout akan segera hadir!", Toast.LENGTH_SHORT).show()
        }
    }
}