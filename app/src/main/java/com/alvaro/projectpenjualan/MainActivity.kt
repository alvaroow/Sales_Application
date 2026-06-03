package com.alvaro.projectpenjualan

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.alvaro.projectpenjualan.kategori.DataKategori
import com.alvaro.projectpenjualan.produk.DataProduk
import com.alvaro.projectpenjualan.cabang.DataCabang
import com.alvaro.projectpenjualan.laporan.DataLaporan
import com.alvaro.projectpenjualan.pegawai.DataPegawai
import com.alvaro.projectpenjualan.transaksi.DataTransaksi
import com.alvaro.projectpenjualan.model.ModelTransaksi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var cardKategori: CardView
    lateinit var cardProduk: CardView
    lateinit var cardCabang: CardView
    lateinit var cardTransaksi: CardView
    lateinit var cardLaporan: CardView
    lateinit var cardPegawai: CardView
    lateinit var btnLogout: android.widget.ImageButton

    // Tambahkan variabel untuk ringkasan
    lateinit var tvGreeting: TextView
    lateinit var tvSaldoValue: TextView
    lateinit var tvStatTransaksi: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        displayUsername()
        fetchRealtimeData()

        cardKategori.setOnClickListener {
            startActivity(Intent(this, DataKategori::class.java))
        }

        cardProduk.setOnClickListener {
            startActivity(Intent(this, DataProduk::class.java))
        }

        cardCabang.setOnClickListener {
            startActivity(Intent(this, DataCabang::class.java))
        }

        cardTransaksi.setOnClickListener {
            startActivity(Intent(this, DataTransaksi::class.java))
        }

        cardLaporan.setOnClickListener {
            startActivity(Intent(this, DataLaporan::class.java))
        }

        cardPegawai.setOnClickListener {
            startActivity(Intent(this, DataPegawai::class.java))
        }

        btnLogout.setOnClickListener {
            // Logika logout: hapus sesi dan kembali ke Login
            val sharedPref = getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                clear()
                apply()
            }
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    fun init() {
        cardKategori = findViewById(R.id.cvKategori)
        cardProduk = findViewById(R.id.cvProduk)
        cardCabang = findViewById(R.id.cvCabang)
        cardTransaksi = findViewById(R.id.cvTransaksi)
        cardLaporan = findViewById(R.id.cvLaporan)
        cardPegawai = findViewById(R.id.cvPegawai)
        btnLogout = findViewById(R.id.btnLogout)
        tvGreeting = findViewById(R.id.tvGreeting)
        tvSaldoValue = findViewById(R.id.tvSaldoValue)
        tvStatTransaksi = findViewById(R.id.tvStatTransaksi)
    }

    private fun displayUsername() {
        val sharedPref = getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Pengguna")
        tvGreeting.text = "Halo, $username"
    }

    private fun fetchRealtimeData() {
        val database = FirebaseDatabase.getInstance().getReference("transaksi")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalPendapatan = 0
                var jumlahOrder = 0

                val calendarSekarang = Calendar.getInstance()
                val hariIni = calendarSekarang.get(Calendar.DAY_OF_YEAR)
                val tahunIni = calendarSekarang.get(Calendar.YEAR)

                for (data in snapshot.children) {
                    val transaksi = data.getValue(ModelTransaksi::class.java)
                    if (transaksi != null) {
                        val calendarTransaksi = Calendar.getInstance()
                        calendarTransaksi.timeInMillis = transaksi.tanggal
                        
                        val hariTransaksi = calendarTransaksi.get(Calendar.DAY_OF_YEAR)
                        val tahunTransaksi = calendarTransaksi.get(Calendar.YEAR)

                        // Hanya hitung jika hari dan tahunnya sama (Harian)
                        if (hariIni == hariTransaksi && tahunIni == tahunTransaksi) {
                            totalPendapatan += transaksi.total
                            jumlahOrder++
                        }
                    }
                }

                // Format ke Rupiah
                val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                tvSaldoValue.text = formatRupiah.format(totalPendapatan)
                    .replace("Rp", "Rp ")
                    .replace(",00", "")
                
                tvStatTransaksi.text = jumlahOrder.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani error jika perlu
            }
        })
    }
}