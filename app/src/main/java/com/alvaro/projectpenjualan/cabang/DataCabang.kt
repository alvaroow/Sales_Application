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

            rvCabang.adapter = AdapterCabang(list)

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