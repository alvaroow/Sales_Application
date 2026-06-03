package com.alvaro.projectpenjualan.transaksi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterStruk
import com.alvaro.projectpenjualan.model.ModelTransaksi
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StrukActivity : AppCompatActivity() {

    private lateinit var tvTanggal: TextView
    private lateinit var tvId: TextView
    private lateinit var tvPelanggan: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvBayar: TextView
    private lateinit var tvKembalian: TextView
    private lateinit var rvStruk: RecyclerView

    private lateinit var btnSelesai: MaterialButton
    private lateinit var btnCetak: MaterialButton

    private var currentTransaksi: ModelTransaksi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_struk)

        tvTanggal = findViewById(R.id.tvTanggal)
        tvId = findViewById(R.id.tvId)
        tvPelanggan = findViewById(R.id.tvPelanggan)
        tvTotal = findViewById(R.id.tvTotal)
        tvBayar = findViewById(R.id.tvBayar)
        tvKembalian = findViewById(R.id.tvKembalian)
        rvStruk = findViewById(R.id.rvStruk)

        btnSelesai = findViewById(R.id.btnSelesai)
        btnCetak = findViewById(R.id.btnCetak)

        rvStruk.layoutManager = LinearLayoutManager(this)

        btnSelesai.setOnClickListener { finish() }
        
        btnCetak.setOnClickListener {
            if (checkPermissions()) {
                currentTransaksi?.let { printBluetooth(it) }
            } else {
                requestPermissions()
            }
        }

        val idTransaksi = intent.getStringExtra("idTransaksi") ?: return

        FirebaseDatabase.getInstance()
            .getReference("transaksi")
            .child(idTransaksi)
            .get()
            .addOnSuccessListener { snapshot ->
                val transaksi = snapshot.getValue(ModelTransaksi::class.java)

                if (transaksi != null) {
                    currentTransaksi = transaksi
                    var namaKasir = transaksi.namaKasir
                    if (namaKasir.isEmpty()) {
                        namaKasir = intent.getStringExtra("NAMA_KASIR") ?: "Kasir Default"
                    }

                    tvId.text = "${transaksi.idTransaksi}\nKasir: $namaKasir"
                    tvPelanggan.text = "Pelanggan: ${transaksi.namaPemesan}"

                    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                    tvTotal.text = formatRupiah.format(transaksi.total)
                        .replace("Rp", "Rp ")
                        .replace(",00", "")
                    tvBayar.text = formatRupiah.format(transaksi.bayar)
                        .replace("Rp", "Rp ")
                        .replace(",00", "")
                    tvKembalian.text = formatRupiah.format(transaksi.kembalian)
                        .replace("Rp", "Rp ")
                        .replace(",00", "")

                    tvTanggal.text = SimpleDateFormat(
                        "dd-MM-yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date(transaksi.tanggal))

                    val adapter = AdapterStruk(transaksi.items)
                    rvStruk.adapter = adapter
                }
            }
    }

    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT),
                1
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    private fun printBluetooth(transaksi: ModelTransaksi) {
        val connection = BluetoothPrintersConnections.selectFirstPaired()
        if (connection != null) {
            try {
                val printer = EscPosPrinter(connection, 203, 48f, 32)
                val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                
                var itemsPrint = ""
                for (item in transaksi.items) {
                    val subtotal = item.qty * (item.produk.hargaProduk ?: 0)
                    itemsPrint += "[L]<b>${item.produk.namaProduk}</b>\n"
                    itemsPrint += "[L]  ${item.qty} x ${formatRupiah.format(item.produk.hargaProduk).replace(",00", "")} [R]${formatRupiah.format(subtotal).replace(",00", "")}\n"
                }

                val datePrint = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(transaksi.tanggal))

                val receipt = 
                    "[C]<b>KASIR KITA</b>\n" +
                    "[C]Jl. Slamet Riyadi, Surakarta\n" +
                    "[C]Telp: 0812-3456-7890\n" +
                    "[C]--------------------------------\n" +
                    "[L]Tgl: $datePrint\n" +
                    "[L]ID : ${transaksi.idTransaksi}\n" +
                    "[L]Kasir: ${transaksi.namaKasir}\n" +
                    "[L]Plg  : ${transaksi.namaPemesan}\n" +
                    "[C]--------------------------------\n" +
                    itemsPrint +
                    "[C]--------------------------------\n" +
                    "[L]<b>TOTAL[R]${formatRupiah.format(transaksi.total).replace(",00", "")}</b>\n" +
                    "[L]TUNAI[R]${formatRupiah.format(transaksi.bayar).replace(",00", "")}\n" +
                    "[L]KEMBALI[R]${formatRupiah.format(transaksi.kembalian).replace(",00", "")}\n" +
                    "[C]--------------------------------\n" +
                    "[C]TERIMA KASIH\n" +
                    "[C]Selamat Belanja Kembali\n"

                printer.printFormattedText(receipt)
                Toast.makeText(this, "Mencetak struk...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal mencetak: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Tidak ada printer bluetooth yang terhubung!", Toast.LENGTH_LONG).show()
        }
    }
}