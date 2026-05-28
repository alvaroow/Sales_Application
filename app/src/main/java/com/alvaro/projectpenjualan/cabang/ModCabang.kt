package com.alvaro.projectpenjualan.cabang

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelCabang
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class ModCabang : AppCompatActivity() {

    lateinit var etNama: TextInputEditText
    lateinit var etAlamat: TextInputEditText
    lateinit var etTelp: TextInputEditText
    lateinit var btnSimpan: MaterialButton

    private val db = FirebaseDatabase.getInstance().getReference("cabang")

    // ✅ Variabel penampung untuk mendeteksi apakah ini sedang Mode Edit
    private var idCabangEdit: String? = null
    private var statusCabangEdit: String = "Aktif"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_cabang)

        init()

        // ✅ Cek apakah ada data yang dibawa dari halaman list (Tanda bahwa ini Mode Edit)
        idCabangEdit = intent.getStringExtra("ID")

        if (idCabangEdit != null) {
            // Isi otomatis form-nya dengan data lama
            etNama.setText(intent.getStringExtra("NAMA"))
            etAlamat.setText(intent.getStringExtra("ALAMAT"))
            etTelp.setText(intent.getStringExtra("TELP"))
            statusCabangEdit = intent.getStringExtra("STATUS") ?: "Aktif"

            // Ubah tulisan tombol
            btnSimpan.text = "Update Cabang"
        }

        btnSimpan.setOnClickListener {
            simpanCabang()
        }
    }

    private fun simpanCabang() {

        val nama = etNama.text.toString()
        val alamat = etAlamat.text.toString()
        val telp = etTelp.text.toString()

        if (nama.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Jika idCabangEdit ada isinya, pakai ID lama (Update). Jika kosong, buat ID baru (Simpan).
        val id = idCabangEdit ?: db.push().key!!

        val cabang = ModelCabang(
            idCabang = id,
            namaCabang = nama,
            alamatCabang = alamat,
            telpCabang = telp,
            statusCabang = statusCabangEdit
        )

        db.child(id).setValue(cabang)
            .addOnSuccessListener {

                // Pesan sukses disesuaikan dengan modenya
                val pesan = if (idCabangEdit != null) "Cabang berhasil diupdate" else "Cabang berhasil disimpan"
                Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()

                finish()
            }
    }

    private fun init() {
        etNama = findViewById(R.id.etNamaCabang)
        etAlamat = findViewById(R.id.etAlamatCabang)
        etTelp = findViewById(R.id.etTelpCabang)
        btnSimpan = findViewById(R.id.btnSimpanCabang)
    }
}