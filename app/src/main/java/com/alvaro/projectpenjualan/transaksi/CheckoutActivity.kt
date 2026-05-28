package com.alvaro.projectpenjualan.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.CartManager
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelTransaksi
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase

class CheckoutActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var tvKembalian: TextView
    private lateinit var etBayar: EditText
    private lateinit var btnBayar: MaterialButton

    private var total = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        tvTotal = findViewById(R.id.tvTotalBayar)
        tvKembalian = findViewById(R.id.tvKembalian)
        etBayar = findViewById(R.id.etBayar)
        btnBayar = findViewById(R.id.btnBayar)

        total = CartManager.getTotal()
        tvTotal.text = "Rp $total"

        val namaKasir = intent.getStringExtra("NAMA_KASIR") ?: "Kasir Default"

        btnBayar.setOnClickListener {

            val bayar = etBayar.text.toString().toIntOrNull() ?: 0

            if (bayar < total) {
                Toast.makeText(this, "Uang kurang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val kembalian = bayar - total
            val idTransaksi = "TXN" + System.currentTimeMillis()

            val transaksi = ModelTransaksi(
                idTransaksi = idTransaksi,
                total = total,
                bayar = bayar,
                kembalian = kembalian,
                tanggal = System.currentTimeMillis(),
                items = CartManager.getAll().map { it.copy() },
                namaKasir = namaKasir // ✅ SIMPAN NAMA KASIR KE DATABASE
            )

            FirebaseDatabase.getInstance()
                .getReference("transaksi")
                .child(idTransaksi)
                .setValue(transaksi)
                .addOnSuccessListener {

                    Toast.makeText(this, "Checkout berhasil", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, StrukActivity::class.java)
                    intent.putExtra("idTransaksi", idTransaksi)
                    intent.putExtra("NAMA_KASIR", namaKasir)
                    startActivity(intent)

                    CartManager.clear()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal simpan transaksi", Toast.LENGTH_SHORT).show()
                }
        }
    }
}