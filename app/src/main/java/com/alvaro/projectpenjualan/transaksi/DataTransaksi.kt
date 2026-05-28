package com.alvaro.projectpenjualan.transaksi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterTransaksi
import com.alvaro.projectpenjualan.model.ModelProduk
import com.google.firebase.database.*

class DataTransaksi : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: AdapterTransaksi
    private val list = ArrayList<ModelProduk>()

    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_transaksi)

        rv = findViewById(R.id.rvTransaksi)

        adapter = AdapterTransaksi(list)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.adapter = adapter

        ref = FirebaseDatabase.getInstance().getReference("produk")

        loadData()
    }

    private fun loadData() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                for (data in snapshot.children) {
                    val produk = data.getValue(ModelProduk::class.java)
                    produk?.let { list.add(it) }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}