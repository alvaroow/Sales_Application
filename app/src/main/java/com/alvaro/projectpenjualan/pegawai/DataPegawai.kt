package com.alvaro.projectpenjualan.pegawai

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterPegawai
import com.alvaro.projectpenjualan.model.ModelPegawai
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class DataPegawai : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var svPegawai: android.widget.SearchView
    private val db = FirebaseDatabase.getInstance().getReference("pegawai")
    private val listPegawai = ArrayList<ModelPegawai>()
    private val fullList = ArrayList<ModelPegawai>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pegawai)

        rv = findViewById(R.id.rvPegawai) // Sesuaikan ID
        fab = findViewById(R.id.fabTambahPegawai) // Sesuaikan ID
        svPegawai = findViewById(R.id.svPegawai)

        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)

        fab.setOnClickListener {
            startActivity(Intent(this, ModPegawai::class.java))
        }

        setupSearchView()
        loadData()
    }

    private fun setupSearchView() {
        svPegawai.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText ?: "")
                return true
            }
        })
    }

    private fun filterData(query: String) {
        val filtered = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter { it.namaPegawai?.contains(query, ignoreCase = true) == true }
        }
        updateRecyclerView(ArrayList(filtered))
    }

    private fun loadData() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fullList.clear()
                for (data in snapshot.children) {
                    val p = data.getValue(ModelPegawai::class.java)
                    if (p != null) fullList.add(p)
                }
                filterData(svPegawai.query.toString())
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateRecyclerView(list: ArrayList<ModelPegawai>) {
        val adapter = AdapterPegawai(list)
        rv.adapter = adapter

        // 1. KLIK BIASA (EDIT)
        adapter.setOnItemClickListener(object : AdapterPegawai.OnItemClickListener {
            override fun onItemClick(pegawai: ModelPegawai) {
                val intent = Intent(this@DataPegawai, ModPegawai::class.java)
                intent.putExtra("ID", pegawai.idPegawai)
                intent.putExtra("NAMA", pegawai.namaPegawai)
                intent.putExtra("TELP", pegawai.noTelp)
                intent.putExtra("STATUS", pegawai.statusPegawai)
                startActivity(intent)
            }
        })

        // 2. TEKAN TAHAN (HAPUS)
        adapter.setOnItemLongClickListener(object : AdapterPegawai.OnItemLongClickListener {
            override fun onItemLongClick(pegawai: ModelPegawai) {
                AlertDialog.Builder(this@DataPegawai)
                    .setTitle("Hapus Pegawai")
                    .setMessage("Yakin ingin menghapus ${pegawai.namaPegawai} secara permanen?")
                    .setPositiveButton("Hapus") { _, _ ->
                        db.child(pegawai.idPegawai ?: "").removeValue().addOnSuccessListener {
                            Toast.makeText(this@DataPegawai, "Pegawai dihapus", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        })

        // 3. UBAH STATUS
        adapter.setOnStatusClickListener(object : AdapterPegawai.OnStatusClickListener {
            override fun onStatusClick(pegawai: ModelPegawai) {
                val statusBaru = if (pegawai.statusPegawai == "Aktif") "Non Aktif" else "Aktif"
                db.child(pegawai.idPegawai ?: "").child("statusPegawai").setValue(statusBaru)
            }
        })
    }
}