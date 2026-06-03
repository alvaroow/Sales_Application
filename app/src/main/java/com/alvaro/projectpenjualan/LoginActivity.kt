package com.alvaro.projectpenjualan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.databinding.ActivityLoginBinding
import com.alvaro.projectpenjualan.model.ModelPegawai
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    
    // Tentukan Password Bersama di sini
    private val PASSWORD_BERSAMA = "12345"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty()) {
                binding.tilUsername.error = "Nama pengguna tidak boleh kosong"
                return@setOnClickListener
            } else {
                binding.tilUsername.error = null
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = "Kata sandi tidak boleh kosong"
                return@setOnClickListener
            } else {
                binding.tilPassword.error = null
            }

            // 1. Cek dulu apakah Password Bersama benar
            if (password != PASSWORD_BERSAMA) {
                binding.tilPassword.error = "Kata sandi salah"
                Toast.makeText(this, "Kata sandi salah!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Jika password benar, cek apakah Nama Pegawai ada di Firebase
            checkUsernameInFirebase(username)
        }
    }

    private fun checkUsernameInFirebase(username: String) {
        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "Memverifikasi Nama..."

        val dbRef = FirebaseDatabase.getInstance().getReference("pegawai")
        
        // Cari di Firebase berdasarkan namaPegawai
        dbRef.orderByChild("namaPegawai").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Masuk Sekarang"

                    if (snapshot.exists()) {
                        // NAMA DITEMUKAN - Berhasil Login
                        saveSession(username)
                        
                        Toast.makeText(this@LoginActivity, "Selamat datang, $username", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // NAMA TIDAK ADA DI DATABASE
                        binding.tilUsername.error = "Nama pegawai tidak terdaftar"
                        Toast.makeText(this@LoginActivity, "Akses ditolak: Nama tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = "Masuk Sekarang"
                    Toast.makeText(this@LoginActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveSession(username: String) {
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", username)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }
}