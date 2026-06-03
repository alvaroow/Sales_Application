package com.alvaro.projectpenjualan.produk

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterProduk
import com.alvaro.projectpenjualan.model.DataProdukViewModel
import com.alvaro.projectpenjualan.model.ModelProduk
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class DataProduk : AppCompatActivity() {

    private val viewModel: DataProdukViewModel by viewModels()
    private lateinit var rvDATAPRODUK: RecyclerView
    private lateinit var fabDATAPRODUKTambah: FloatingActionButton
    private lateinit var svProduk: android.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_produk)

        init()
        setupSearchView()

        rvDATAPRODUK.layoutManager = LinearLayoutManager(this)
        rvDATAPRODUK.setHasFixedSize(true)

        viewModel.produkList.observe(this) { list ->
            val adapter = AdapterProduk(list)
            rvDATAPRODUK.adapter = adapter


            adapter.setOnItemClickListener(object : AdapterProduk.OnItemClickListener {
                override fun onItemClick(produk: ModelProduk) {
                    val intent = Intent(this@DataProduk, ModProduk::class.java)
                    intent.putExtra("ID", produk.idProduk)
                    intent.putExtra("NAMA", produk.namaProduk)
                    intent.putExtra("HARGA", produk.hargaProduk.toString())
                    intent.putExtra("KATEGORI", produk.idKategori)
                    intent.putExtra("CABANG", produk.idCabang)
                    intent.putExtra("STOK", produk.stokProduk.toString())
                    intent.putExtra("STATUS", produk.statusProduk)
                    intent.putExtra("FOTO", produk.fotoProduk)
                    startActivity(intent)
                }
            })


            adapter.setOnItemLongClickListener(object : AdapterProduk.OnItemLongClickListener {
                override fun onItemLongClick(produk: ModelProduk) {
                    AlertDialog.Builder(this@DataProduk)
                        .setTitle("Hapus Produk")
                        .setMessage("Yakin ingin menghapus '${produk.namaProduk}' secara permanen?")
                        .setPositiveButton("Hapus") { _, _ ->
                            FirebaseDatabase.getInstance().getReference("produk")
                                .child(produk.idProduk ?: "")
                                .removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(this@DataProduk, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .setNegativeButton("Batal", null)
                        .show()
                }
            })


            adapter.setOnStatusClickListener(object : AdapterProduk.OnStatusClickListener {
                override fun onStatusClick(produk: ModelProduk) {
                    viewModel.toggleStatus(produk)
                }
            })
        }
    }

    private fun init() {
        rvDATAPRODUK = findViewById(R.id.rvDATA_PRODUK)
        fabDATAPRODUKTambah = findViewById(R.id.fabDATA_PRODUK_Tambah)
        svProduk = findViewById(R.id.svProduk)

        fabDATAPRODUKTambah.setOnClickListener {
            startActivity(Intent(this, ModProduk::class.java))
        }
    }

    private fun setupSearchView() {
        svProduk.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchProduk(newText ?: "")
                return true
            }
        })
    }
}