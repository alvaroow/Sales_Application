package com.alvaro.projectpenjualan.kategori

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alvaro.projectpenjualan.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DataKategori : AppCompatActivity() {
    private lateinit var fabData: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_kategori)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()

        fabData = findViewById(R.id.fabDATA_KATEGORI_Tambah)
        fabData.setOnClickListener {
            val intent = Intent(this, ModKategori::class.java)
            startActivity(intent)
        }
    }
    fun init(){
        fabData = findViewById(R.id.fabDATA_KATEGORI_Tambah)

    }
}

