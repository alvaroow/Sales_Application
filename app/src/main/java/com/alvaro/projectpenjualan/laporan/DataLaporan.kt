package com.alvaro.projectpenjualan.laporan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterLaporan
import com.alvaro.projectpenjualan.model.ModelTransaksi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataLaporan : AppCompatActivity() {

    private lateinit var rvLaporan: RecyclerView
    private val listRiwayat = ArrayList<ModelTransaksi>()
    private lateinit var adapter: AdapterLaporan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_laporan)

        rvLaporan = findViewById(R.id.rvLaporan)
        rvLaporan.layoutManager = LinearLayoutManager(this)

        adapter = AdapterLaporan(listRiwayat)
        rvLaporan.adapter = adapter

        // Tarik data dari Firebase
        FirebaseDatabase.getInstance().getReference("transaksi")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listRiwayat.clear()

                    for (data in snapshot.children) {
                        val transaksi = data.getValue(ModelTransaksi::class.java)
                        if (transaksi != null) {
                            listRiwayat.add(transaksi)
                        }
                    }

                    // Supaya yang terbaru ada di atas
                    listRiwayat.reverse()
                    adapter.notifyDataSetChanged()

                    // ... di dalam onCreate DataLaporan.kt setelah set adapter

                    adapter.setOnItemLongClickListener(object : AdapterLaporan.OnItemLongClickListener {
                        override fun onItemLongClick(transaksi: ModelTransaksi) {
                            // ✅ 3. Dialog Konfirmasi
                            androidx.appcompat.app.AlertDialog.Builder(this@DataLaporan)
                                .setTitle("Hapus Transaksi")
                                .setMessage("Yakin ingin menghapus transaksi ini?")
                                .setPositiveButton("Hapus") { _, _ ->
                                    // ✅ 4. Proses Hapus dari Firebase
                                    FirebaseDatabase.getInstance().getReference("transaksi")
                                        .child(transaksi.idTransaksi ?: "")
                                        .removeValue()
                                        .addOnSuccessListener {
                                            Toast.makeText(this@DataLaporan, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(this@DataLaporan, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .setNegativeButton("Batal", null)
                                .show()
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    // Biarkan kosong untuk MVP
                }
            })
    }
}