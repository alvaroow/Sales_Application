package com.alvaro.projectpenjualan.kategori

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterKategori
import com.alvaro.projectpenjualan.model.DataKategoriViewModel
import com.alvaro.projectpenjualan.model.ModelKategori
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class DataKategori : AppCompatActivity() {

    private val viewModel: DataKategoriViewModel by viewModels()
    private lateinit var rvDATAKATEGORI: RecyclerView
    private lateinit var fabDATAKATEGORITambah: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_kategori)

        init()

        // ✅ Sudah dikembalikan ke tampilan list memanjang ke bawah
        rvDATAKATEGORI.layoutManager = LinearLayoutManager(this)
        rvDATAKATEGORI.setHasFixedSize(true)

        viewModel.kategoriList.observe(this) { list ->
            val adapter = AdapterKategori(list)

            rvDATAKATEGORI.adapter = adapter

            // FUNGSI EDIT (KLIK BIASA)
            adapter.setOnItemClickListener(object : AdapterKategori.OnItemClickListener {
                override fun onItemClick(kategori: ModelKategori) {
                    val intent = Intent(this@DataKategori, ModKategori::class.java)
                    intent.putExtra("ID", kategori.idKategori)
                    intent.putExtra("NAMA", kategori.namaKategori)
                    intent.putExtra("STATUS", kategori.statusKategori)
                    startActivity(intent)
                }
            })

            // FUNGSI HAPUS (TEKAN TAHAN)
            adapter.setOnItemLongClickListener(object : AdapterKategori.OnItemLongClickListener {
                override fun onItemLongClick(kategori: ModelKategori) {
                    AlertDialog.Builder(this@DataKategori)
                        .setTitle("Hapus Kategori")
                        .setMessage("Yakin ingin menghapus kategori '${kategori.namaKategori}' secara permanen?")
                        .setPositiveButton("Hapus") { _, _ ->
                            FirebaseDatabase.getInstance().getReference("kategori")
                                .child(kategori.idKategori ?: "")
                                .removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(this@DataKategori, "Kategori berhasil dihapus", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .setNegativeButton("Batal", null)
                        .show()
                }
            })

            // FUNGSI UBAH STATUS
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