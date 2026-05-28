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

        btnSelesai.setOnClickListener { finish() }
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
                    // ✅ PRIORITASKAN NAMA KASIR DARI DATABASE
                    var namaKasir = transaksi.namaKasir
                    if (namaKasir.isEmpty()) {
                        namaKasir = intent.getStringExtra("NAMA_KASIR") ?: "Kasir Default"
                    }

                    tvId.text = "${transaksi.idTransaksi}\nKasir: $namaKasir"

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