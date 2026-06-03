package com.alvaro.projectpenjualan.laporan

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterLaporan
import com.alvaro.projectpenjualan.model.ModelTransaksi
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class DataLaporan : AppCompatActivity() {

    private lateinit var rvLaporan: RecyclerView
    private lateinit var svLaporan: android.widget.SearchView
    private lateinit var chipGroupFilter: ChipGroup
    private lateinit var tvTotalPendapatan: TextView
    private lateinit var tvTotalOrder: TextView
    private val listRiwayat = ArrayList<ModelTransaksi>()
    private val fullList = ArrayList<ModelTransaksi>()
    private lateinit var adapter: AdapterLaporan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_laporan)

        rvLaporan = findViewById(R.id.rvLaporan)
        svLaporan = findViewById(R.id.svLaporan)
        chipGroupFilter = findViewById(R.id.chipGroupFilter)
        tvTotalPendapatan = findViewById(R.id.tvTotalPendapatan)
        tvTotalOrder = findViewById(R.id.tvTotalOrder)
        
        rvLaporan.layoutManager = LinearLayoutManager(this)

        adapter = AdapterLaporan(listRiwayat)
        rvLaporan.adapter = adapter

        setupSearchView()
        setupFilter()
        loadData()
    }

    private fun setupFilter() {
        chipGroupFilter.setOnCheckedChangeListener { _, checkedId ->
            filterData(svLaporan.query.toString())
        }
    }

    private fun setupSearchView() {
        svLaporan.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText ?: "")
                return true
            }
        })
    }

    private fun filterData(query: String) {
        val calendarSekarang = Calendar.getInstance()
        val hariIni = calendarSekarang.get(Calendar.DAY_OF_YEAR)
        val bulanIni = calendarSekarang.get(Calendar.MONTH)
        val tahunIni = calendarSekarang.get(Calendar.YEAR)

        val filtered = fullList.filter { transaksi ->
            // Filter Berdasarkan Search
            val matchesSearch = transaksi.idTransaksi.contains(query, ignoreCase = true)

            // Filter Berdasarkan Waktu
            val calendarTransaksi = Calendar.getInstance()
            calendarTransaksi.timeInMillis = transaksi.tanggal
            
            val isDateMatch = when (chipGroupFilter.checkedChipId) {
                R.id.chipToday -> {
                    calendarTransaksi.get(Calendar.DAY_OF_YEAR) == hariIni &&
                            calendarTransaksi.get(Calendar.YEAR) == tahunIni
                }
                R.id.chipMonth -> {
                    calendarTransaksi.get(Calendar.MONTH) == bulanIni &&
                            calendarTransaksi.get(Calendar.YEAR) == tahunIni
                }
                else -> true // chipAll
            }

            matchesSearch && isDateMatch
        }
        
        listRiwayat.clear()
        listRiwayat.addAll(filtered)
        adapter.notifyDataSetChanged()
        updateSummary()
    }

    private fun updateSummary() {
        var totalPendapatan = 0
        for (transaksi in listRiwayat) {
            totalPendapatan += transaksi.total
        }

        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        tvTotalPendapatan.text = formatRupiah.format(totalPendapatan)
            .replace("Rp", "Rp ")
            .replace(",00", "")
        tvTotalOrder.text = listRiwayat.size.toString()
    }

    private fun loadData() {
        FirebaseDatabase.getInstance().getReference("transaksi")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    fullList.clear()

                    for (data in snapshot.children) {
                        val transaksi = data.getValue(ModelTransaksi::class.java)
                        if (transaksi != null) {
                            fullList.add(transaksi)
                        }
                    }

                    // Supaya yang terbaru ada di atas
                    fullList.reverse()
                    filterData(svLaporan.query.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    // Biarkan kosong untuk MVP
                }
            })
    }
}
