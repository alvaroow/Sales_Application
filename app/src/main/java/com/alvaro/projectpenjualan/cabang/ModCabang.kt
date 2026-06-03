package com.alvaro.projectpenjualan.cabang

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelCabang
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class ModCabang : AppCompatActivity() {

    lateinit var tvHeader: TextView
    lateinit var etNama: TextInputEditText
    lateinit var etAlamat: TextInputEditText
    lateinit var etTelp: TextInputEditText
    lateinit var spStatus: AutoCompleteTextView
    lateinit var btnSimpan: MaterialButton

    private val db = FirebaseDatabase.getInstance().getReference("cabang")

    private var idCabangEdit: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_cabang)

        init()


        val statusList = arrayOf("Aktif", "Non Aktif")
        val adapterStatus = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusList)
        spStatus.setAdapter(adapterStatus)

        idCabangEdit = intent.getStringExtra("ID")

        if (idCabangEdit != null) {
            tvHeader.text = "Edit Cabang"
            etNama.setText(intent.getStringExtra("NAMA"))
            etAlamat.setText(intent.getStringExtra("ALAMAT"))
            etTelp.setText(intent.getStringExtra("TELP"))

            // Set status lama di form
            val statusLama = intent.getStringExtra("STATUS") ?: "Aktif"
            spStatus.setText(statusLama, false)

            btnSimpan.text = "Update Cabang"
        } else {
            // Set default saat tambah baru
            spStatus.setText("Aktif", false)
        }

        btnSimpan.setOnClickListener {
            simpanCabang()
        }
    }

    private fun simpanCabang() {
        val nama = etNama.text.toString()
        val alamat = etAlamat.text.toString()
        val telp = etTelp.text.toString()
        val status = spStatus.text.toString()

        if (nama.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idCabangEdit ?: db.push().key!!

        val cabang = ModelCabang(
            idCabang = id,
            namaCabang = nama,
            alamatCabang = alamat,
            telpCabang = telp,
            statusCabang = status
        )

        db.child(id).setValue(cabang)
            .addOnSuccessListener {
                val pesan = if (idCabangEdit != null) "Cabang berhasil diupdate" else "Cabang berhasil disimpan"
                Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun init() {
        tvHeader = findViewById(R.id.tvHeader)
        etNama = findViewById(R.id.etNamaCabang)
        etAlamat = findViewById(R.id.etAlamatCabang)
        etTelp = findViewById(R.id.etTelpCabang)
        spStatus = findViewById(R.id.spStatusCabang)
        btnSimpan = findViewById(R.id.btnSimpanCabang)
    }
}