package com.alvaro.projectpenjualan.pegawai

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelPegawai
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class ModPegawai : AppCompatActivity() {

    private lateinit var tvHeader: TextView
    private lateinit var etNama: TextInputEditText
    private lateinit var etTelp: TextInputEditText
    private lateinit var spStatus: AutoCompleteTextView
    private lateinit var btnSimpan: MaterialButton

    private val db = FirebaseDatabase.getInstance().getReference("pegawai")

    private var idPegawaiEdit: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_pegawai)

        tvHeader = findViewById(R.id.tvHeader)
        etNama = findViewById(R.id.etNamaPegawai)
        etTelp = findViewById(R.id.etNoTelp)
        spStatus = findViewById(R.id.spStatusPegawai)
        btnSimpan = findViewById(R.id.btnSimpanPegawai)


        val statusList = arrayOf("Aktif", "Non Aktif")
        val adapterStatus = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusList)
        spStatus.setAdapter(adapterStatus)

        idPegawaiEdit = intent.getStringExtra("ID")
        if (idPegawaiEdit != null) {
            tvHeader.text = "Edit Pegawai"
            etNama.setText(intent.getStringExtra("NAMA"))
            etTelp.setText(intent.getStringExtra("TELP"))

            // Set status lama di form
            val statusLama = intent.getStringExtra("STATUS") ?: "Aktif"
            spStatus.setText(statusLama, false)

            btnSimpan.text = "Update Pegawai"
        } else {
            // Set default saat tambah baru
            spStatus.setText("Aktif", false)
        }

        btnSimpan.setOnClickListener {
            saveOrUpdate()
        }
    }

    private fun saveOrUpdate() {
        val nama = etNama.text.toString()
        val telp = etTelp.text.toString()
        val status = spStatus.text.toString()

        if (nama.isEmpty() || telp.isEmpty()) {
            Toast.makeText(this, "Lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        val id = idPegawaiEdit ?: db.push().key ?: return
        val pegawai = ModelPegawai(id, nama, telp, status)

        db.child(id).setValue(pegawai).addOnSuccessListener {
            val pesan = if (idPegawaiEdit != null) "Pegawai diupdate" else "Pegawai disimpan"
            Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}