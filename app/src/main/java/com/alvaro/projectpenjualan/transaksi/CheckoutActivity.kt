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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var tvKembalian: TextView
    private lateinit var etBayar: EditText
    private lateinit var etNamaPemesan: EditText
    private lateinit var btnBayar: MaterialButton

    private var total = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        tvTotal = findViewById(R.id.tvTotalBayar)
        tvKembalian = findViewById(R.id.tvKembalian)
        etBayar = findViewById(R.id.etBayar)
        etNamaPemesan = findViewById(R.id.etNamaPemesan)
        btnBayar = findViewById(R.id.btnBayar)

        total = CartManager.getTotal()
        
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        tvTotal.text = formatRupiah.format(total)
            .replace("Rp", "Rp ")
            .replace(",00", "")

        val namaKasir = intent.getStringExtra("NAMA_KASIR") ?: "Kasir Default"

        btnBayar.setOnClickListener {
            val bayar = etBayar.text.toString().toIntOrNull() ?: 0
            val namaPemesan = etNamaPemesan.text.toString().trim().ifEmpty { "-" }

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
                namaKasir = namaKasir,
                namaPemesan = namaPemesan
            )

            FirebaseDatabase.getInstance()
                .getReference("transaksi")
                .child(idTransaksi)
                .setValue(transaksi)
                .addOnSuccessListener {

                    updateStokDiFirebase()

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

    //  FUNGSI UNTUK MENGURANGI STOK DI FIREBASE
    private fun updateStokDiFirebase() {
        val database = FirebaseDatabase.getInstance().getReference("produk")

        for (item in CartManager.getAll()) {
            // Karena strukturmu: item.produk.idProduk
            val id = item.produk.idProduk ?: continue

            // Karena strukturmu: item.qty
            val jumlahDibeli = item.qty

            database.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Ambil stok dari Firebase
                    val stokLama = snapshot.child("stokProduk").getValue(Int::class.java) ?: 0

                    // Cek jika stok lebih besar dari 0 (bukan stok tak terbatas)
                    if (stokLama > 0) {
                        val stokBaru = stokLama - jumlahDibeli

                        // Update ke Firebase
                        database.child(id).child("stokProduk").setValue(stokBaru)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error jika perlu
                }
            })
        }
    }
}