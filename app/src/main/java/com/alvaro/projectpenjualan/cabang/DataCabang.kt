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
    private lateinit var svCabang: android.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_cabang)

        init()
        setupRecycler()
        observeData()
        setupListener()
        setupSearchView()
    }

    private fun init() {
        rvCabang = findViewById(R.id.rvCabang)
        fabTambah = findViewById(R.id.fabTambahCabang)
        tvHeader = findViewById(R.id.tvHeader)
        svCabang = findViewById(R.id.svCabang)
    }

    private fun setupRecycler() {
        rvCabang.layoutManager = LinearLayoutManager(this)
    }

    private fun observeData() {
        vm.cabangList.observe(this) { list ->

            val adapterCabang = AdapterCabang(list)

            // 1. Fungsi Hapus (Tekan Tahan)
            adapterCabang.setOnItemLongClickListener(object : AdapterCabang.OnItemLongClickListener {
                override fun onItemLongClick(cabang: com.alvaro.projectpenjualan.model.ModelCabang) {
                    // Memunculkan Pop-up konfirmasi
                    android.app.AlertDialog.Builder(this@DataCabang)
                        .setTitle("Hapus Cabang")
                        .setMessage("Yakin ingin menghapus ${cabang.namaCabang} secara permanen?")
                        .setPositiveButton("Hapus") { _, _ ->
                            // Perintah Hapus ke Firebase
                            com.google.firebase.database.FirebaseDatabase.getInstance()
                                .getReference("cabang")
                                .child(cabang.idCabang ?: "")
                                .removeValue()
                                .addOnSuccessListener {
                                    android.widget.Toast.makeText(this@DataCabang, "Cabang dihapus", android.widget.Toast.LENGTH_SHORT).show()
                                }
                        }
                        .setNegativeButton("Batal", null)
                        .show()
                }
            })

            // 2. Fungsi Edit (Klik Biasa)
            adapterCabang.setOnItemClickListener(object : AdapterCabang.OnItemClickListener {
                override fun onItemClick(cabang: com.alvaro.projectpenjualan.model.ModelCabang) {
                    val intent = android.content.Intent(this@DataCabang, ModCabang::class.java)
                    // Bawa data lama ke halaman Form
                    intent.putExtra("ID", cabang.idCabang)
                    intent.putExtra("NAMA", cabang.namaCabang)
                    intent.putExtra("ALAMAT", cabang.alamatCabang)
                    intent.putExtra("STATUS", cabang.statusCabang)
                    startActivity(intent)
                }
            })

            // 3. Fungsi Ubah Status
            adapterCabang.setOnStatusClickListener(object : AdapterCabang.OnStatusClickListener {
                override fun onStatusClick(cabang: com.alvaro.projectpenjualan.model.ModelCabang) {
                    vm.toggleStatus(cabang)
                }
            })

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

    private fun setupSearchView() {
        svCabang.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                vm.filterList(newText)
                return true
            }
        })
    }
}