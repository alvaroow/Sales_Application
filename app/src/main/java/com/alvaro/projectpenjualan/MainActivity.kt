package com.alvaro.projectpenjualan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alvaro.projectpenjualan.kategori.DataKategori
import com.alvaro.projectpenjualan.produk.DataProduk
import com.alvaro.projectpenjualan.cabang.DataCabang

class MainActivity : AppCompatActivity() {

    lateinit var CardKategori : CardView
    lateinit var CardProduk : CardView

    lateinit var CardCabang : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        init()

        CardKategori.setOnClickListener {
            val intent = Intent(this@MainActivity, DataKategori::class.java)
            startActivity(intent)
        }
        CardProduk.setOnClickListener {
            val intent = Intent(this@MainActivity, DataProduk::class.java)
            startActivity(intent)
        }
        CardCabang.setOnClickListener {
            val intent = Intent(this@MainActivity, DataCabang::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }

    fun init() {
        CardKategori = findViewById(R.id.cv4)
        CardProduk = findViewById(R.id.cv3)
        CardCabang = findViewById(R.id.cv6)
    }
}