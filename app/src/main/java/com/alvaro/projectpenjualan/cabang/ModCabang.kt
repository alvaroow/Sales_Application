package com.alvaro.projectpenjualan.cabang

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelCabang
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class ModCabang :
    AppCompatActivity() {

    lateinit var etNama:
            TextInputEditText

    lateinit var etAlamat:
            TextInputEditText

    lateinit var etTelp:
            TextInputEditText

    lateinit var btnSimpan:
            MaterialButton

    private val db =

        FirebaseDatabase
            .getInstance()
            .getReference(
                "cabang"
            )

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_mod_cabang
        )

        init()

        btnSimpan
            .setOnClickListener {

                simpanCabang()

            }

    }

    private fun simpanCabang(){

        val nama =
            etNama.text.toString()

        val alamat =
            etAlamat.text.toString()

        val telp =
            etTelp.text.toString()

        if(
            nama.isEmpty()
            ||
            alamat.isEmpty()
        ){

            Toast.makeText(
                this,
                "Lengkapi data",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val id =
            db.push().key!!

        val cabang =

            ModelCabang(

                idCabang =
                    id,

                namaCabang =
                    nama,

                alamatCabang =
                    alamat,

                telpCabang =
                    telp,

                statusCabang =
                    "Aktif"

            )

        db.child(id)
            .setValue(
                cabang
            )

            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Cabang berhasil disimpan",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            }

    }

    private fun init(){

        etNama =
            findViewById(
                R.id.etNamaCabang
            )

        etAlamat =
            findViewById(
                R.id.etAlamatCabang
            )

        etTelp =
            findViewById(
                R.id.etTelpCabang
            )

        btnSimpan =
            findViewById(
                R.id.btnSimpanCabang
            )

    }

}