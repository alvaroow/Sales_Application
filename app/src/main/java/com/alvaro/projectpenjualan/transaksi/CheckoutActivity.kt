package com.alvaro.projectpenjualan.transaksi

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.CartManager
import com.alvaro.projectpenjualan.R
import com.google.android.material.button.MaterialButton

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

        btnBayar.setOnClickListener {

            val bayar = etBayar.text.toString().toIntOrNull() ?: 0

            if (bayar < total) {
                Toast.makeText(this, "Uang kurang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val kembalian = bayar - total

            tvKembalian.text = "Rp $kembalian"

            Toast.makeText(this, "Pembayaran berhasil", Toast.LENGTH_LONG).show()

            CartManager.clear()
        }
    }
}