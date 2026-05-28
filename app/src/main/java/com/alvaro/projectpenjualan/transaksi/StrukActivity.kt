package com.alvaro.projectpenjualan.transaksi

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterStruk
import com.alvaro.projectpenjualan.model.ModelTransaksi
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StrukActivity : AppCompatActivity() {

    private lateinit var tvTanggal: TextView
    private lateinit var tvId: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvBayar: TextView
    private lateinit var tvKembalian: TextView
    private lateinit var rvStruk: RecyclerView

    // Variabel Tombol Baru
    private lateinit var btnSelesai: MaterialButton
    private lateinit var btnCetak: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_struk)

        tvTanggal = findViewById(R.id.tvTanggal)
        tvId = findViewById(R.id.tvId)
        tvTotal = findViewById(R.id.tvTotal)
        tvBayar = findViewById(R.id.tvBayar)
        tvKembalian = findViewById(R.id.tvKembalian)
        rvStruk = findViewById(R.id.rvStruk)

        btnSelesai = findViewById(R.id.btnSelesai)
        btnCetak = findViewById(R.id.btnCetak)

        rvStruk.layoutManager = LinearLayoutManager(this)

        // AKSI TOMBOL SELESAI
        btnSelesai.setOnClickListener {
            // Menutup halaman struk dan kembali ke menu sebelumnya
            finish()
        }

        // AKSI TOMBOL CETAK STRUK (Pop-up Dummy untuk demo)
        btnCetak.setOnClickListener {
            Toast.makeText(this, "Menghubungkan ke Printer Bluetooth...", Toast.LENGTH_SHORT).show()
        }

        val idTransaksi = intent.getStringExtra("idTransaksi") ?: return

        FirebaseDatabase.getInstance()
            .getReference("transaksi")
            .child(idTransaksi)
            .get()
            .addOnSuccessListener { snapshot ->
                val transaksi = snapshot.getValue(ModelTransaksi::class.java)

                if (transaksi != null) {
                    tvId.text = transaksi.idTransaksi
                    tvTotal.text = "Rp ${transaksi.total}"
                    tvBayar.text = "Rp ${transaksi.bayar}"
                    tvKembalian.text = "Rp ${transaksi.kembalian}"

                    tvTanggal.text = SimpleDateFormat(
                        "dd-MM-yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date(transaksi.tanggal))

                    val adapter = AdapterStruk(transaksi.items)
                    rvStruk.adapter = adapter
                }
            }
    }
}