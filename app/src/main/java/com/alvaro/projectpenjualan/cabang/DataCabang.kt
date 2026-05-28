package com.alvaro.projectpenjualan.cabang

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterCabang
import com.alvaro.projectpenjualan.model.DataCabangViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DataCabang : AppCompatActivity() {

    private val vm: DataCabangViewModel by viewModels()

    private lateinit var rvCabang: RecyclerView
    private lateinit var fabTambah: FloatingActionButton
    private lateinit var tvHeader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_cabang)

        init()
        setupRecycler()
        observeData()
        setupListener()
    }

    private fun init() {
        rvCabang = findViewById(R.id.rvCabang)
        fabTambah = findViewById(R.id.fabTambahCabang)
        tvHeader = findViewById(R.id.tvHeader)
    }

    private fun setupRecycler() {
        rvCabang.layoutManager = LinearLayoutManager(this)
    }

    private fun observeData() {
        vm.cabangList.observe(this) { list ->

            // 1. Buat variabel adapternya dulu
            val adapterCabang = AdapterCabang(list)

            // 2. Sambungkan kabel klik status ke ViewModel
            adapterCabang.setOnStatusClickListener(object : AdapterCabang.OnStatusClickListener {
                override fun onStatusClick(cabang: com.alvaro.projectpenjualan.model.ModelCabang) {
                    // Perintah ini yang bikin data di Firebase dan layar berubah
                    vm.toggleStatus(cabang)
                }
            })

            // 3. Masukkan adapter yang sudah nyala kabelnya ke RecyclerView
            rvCabang.adapter = adapterCabang
        }
    }

    private fun setupListener() {
        fabTambah.setOnClickListener {
            startActivity(
                Intent(this, ModCabang::class.java)
            )
        }
    }
}