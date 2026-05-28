package com.alvaro.projectpenjualan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.alvaro.projectpenjualan.akun.DataAkun
import com.alvaro.projectpenjualan.kategori.DataKategori
import com.alvaro.projectpenjualan.produk.DataProduk
import com.alvaro.projectpenjualan.cabang.DataCabang
import com.alvaro.projectpenjualan.laporan.DataLaporan
import com.alvaro.projectpenjualan.pegawai.DataPegawai
import com.alvaro.projectpenjualan.transaksi.DataTransaksi

class MainActivity : AppCompatActivity() {

    lateinit var CardKategori: CardView
    lateinit var CardProduk: CardView
    lateinit var CardCabang: CardView
    lateinit var CardTransaksi: CardView
    lateinit var CardLaporan: CardView
    lateinit var CardAkun: CardView
    lateinit var CardPegawai: CardView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        CardKategori.setOnClickListener {
            startActivity(Intent(this, DataKategori::class.java))
        }

        CardProduk.setOnClickListener {
            startActivity(Intent(this, DataProduk::class.java))
        }

        CardCabang.setOnClickListener {
            startActivity(Intent(this, DataCabang::class.java))
        }

        CardTransaksi.setOnClickListener {
            startActivity(Intent(this, DataTransaksi::class.java))
        }

        CardAkun.setOnClickListener {
            startActivity(Intent(this, DataAkun::class.java))
        }

        CardLaporan.setOnClickListener {
            startActivity(Intent(this, DataLaporan::class.java))
        }

        CardPegawai.setOnClickListener {
            startActivity(Intent(this, DataPegawai::class.java))
        }
    }

    fun init() {
        CardKategori = findViewById(R.id.cv4)
        CardProduk = findViewById(R.id.cv3)
        CardCabang = findViewById(R.id.cv6)
        CardTransaksi = findViewById(R.id.cvTransaksi)
        CardLaporan = findViewById(R.id.cvLaporan)
        CardAkun = findViewById(R.id.cv2)
        CardPegawai = findViewById(R.id.cv5)

    }
}