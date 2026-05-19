package com.alvaro.projectpenjualan.kategori

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterKategori
import com.alvaro.projectpenjualan.model.DataKategoriViewModel
import com.alvaro.projectpenjualan.model.ModelKategori
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DataKategori : AppCompatActivity() {

    private val viewModel: DataKategoriViewModel by viewModels()
    private lateinit var rvDATAKATEGORI: RecyclerView
    private lateinit var fabDATAKATEGORITambah: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_kategori)

        init()

        rvDATAKATEGORI.layoutManager = LinearLayoutManager(this)
        rvDATAKATEGORI.setHasFixedSize(true)

        viewModel.kategoriList.observe(this) { list ->
            val adapter = AdapterKategori(list)

            rvDATAKATEGORI.adapter = adapter

            // detail click
            adapter.setOnItemClickListener(object : AdapterKategori.OnItemClickListener {
                override fun onItemClick(kategori: ModelKategori) {
                    Toast.makeText(
                        this@DataKategori,
                        kategori.namaKategori,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


            adapter.setOnStatusClickListener(object : AdapterKategori.OnStatusClickListener {
                override fun onStatusClick(kategori: ModelKategori) {
                    viewModel.toggleStatus(kategori)
                }
            })
        }
    }

    private fun init() {
        rvDATAKATEGORI = findViewById(R.id.rvDATA_KATEGORI)
        fabDATAKATEGORITambah = findViewById(R.id.fabDATA_KATEGORI_Tambah)

        fabDATAKATEGORITambah.setOnClickListener {
            startActivity(Intent(this, ModKategori::class.java))
        }
    }
}