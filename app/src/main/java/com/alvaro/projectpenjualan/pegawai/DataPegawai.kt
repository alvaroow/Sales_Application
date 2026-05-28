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
    private val db = FirebaseDatabase.getInstance().getReference("pegawai")
    private val listPegawai = ArrayList<ModelPegawai>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pegawai)

        rv = findViewById(R.id.rvPegawai) // Sesuaikan ID
        fab = findViewById(R.id.fabTambahPegawai) // Sesuaikan ID

        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)

        fab.setOnClickListener {
            startActivity(Intent(this, ModPegawai::class.java))
        }

        loadData()
    }

    private fun loadData() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPegawai.clear()
                for (data in snapshot.children) {
                    val p = data.getValue(ModelPegawai::class.java)
                    if (p != null) listPegawai.add(p)
                }

                val adapter = AdapterPegawai(listPegawai)
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
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}