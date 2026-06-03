package com.alvaro.projectpenjualan.kategori

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alvaro.projectpenjualan.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class ModKategori : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("kategori")

    private lateinit var tvJudul : TextView
    private lateinit var etNamaKategori : TextInputEditText
    private lateinit var tvStatusKategori : TextInputLayout
    private lateinit var spStatusKategori : AutoCompleteTextView
    private lateinit var btSimpan : MaterialButton


    private var idKategoriEdit: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mod_kategori)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvJudul = findViewById(R.id.tvHeader)
        etNamaKategori = findViewById(R.id.etNamaKategori)
        tvStatusKategori = findViewById(R.id.tilModKategori_Status)
        spStatusKategori = findViewById(R.id.spModKategori_Status)
        btSimpan = findViewById(R.id.btnModKategori_Simpan)

        val statusList = resources.getStringArray(R.array.Status)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,statusList)
        spStatusKategori.setAdapter(adapter)


        idKategoriEdit = intent.getStringExtra("ID")
        if (idKategoriEdit != null) {
            tvJudul.text = "Edit Kategori"
            etNamaKategori.setText(intent.getStringExtra("NAMA"))
            val status = intent.getStringExtra("STATUS") ?: "Aktif"
            spStatusKategori.setText(status, false)
            btSimpan.text = "Update Kategori"
        } else {
            tvJudul.text = "Tambah Kategori"
            if (statusList.isNotEmpty()) spStatusKategori.setText(statusList[0],false)
        }

        btSimpan.setOnClickListener {
            val nama = etNamaKategori.text.toString().trim()
            val status = spStatusKategori.text.toString()

            if (nama.isEmpty()) {
                etNamaKategori.error = "Nama kategori wajib diisi"
                return@setOnClickListener
            }


            val key = idKategoriEdit ?: myRef.push().key

            if (key != null) {
                val kategoriData = mapOf(
                    "idKategori" to key,
                    "namaKategori" to nama,
                    "statusKategori" to status
                )

                myRef.child(key).setValue(kategoriData)
                    .addOnSuccessListener {
                        val pesan = if (idKategoriEdit != null) "Kategori diupdate" else "Kategori disimpan"
                        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menyimpan kategori: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}