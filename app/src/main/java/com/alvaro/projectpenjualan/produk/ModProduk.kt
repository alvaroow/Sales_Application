package com.alvaro.projectpenjualan.produk

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import com.alvaro.projectpenjualan.model.ModelProduk

class ModProduk : AppCompatActivity() {

    private lateinit var ivPreview: ImageView
    private lateinit var etNama: TextInputEditText
    private lateinit var etHarga: TextInputEditText
    private lateinit var etStok: TextInputEditText
    private lateinit var cbUnlimited: CheckBox
    private lateinit var btnSimpan: MaterialButton
    private lateinit var btnCamera: MaterialButton
    private lateinit var btnGallery: MaterialButton
    private lateinit var spKategori: AutoCompleteTextView
    private lateinit var spCabang: AutoCompleteTextView

    private var imageUri: Uri? = null

    private val database = FirebaseDatabase.getInstance()

    private val myRef = database.getReference("produk")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_produk)

        initView()
        setupDropdown()
        setupListener()
    }

    private fun initView() {
        ivPreview = findViewById(R.id.ivPreview)
        etNama = findViewById(R.id.etNama)
        etHarga = findViewById(R.id.etHarga)
        etStok = findViewById(R.id.etStok)
        cbUnlimited = findViewById(R.id.cbUnlimited)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnCamera = findViewById(R.id.btnCamera)
        btnGallery = findViewById(R.id.btnGallery)
        spKategori = findViewById(R.id.spKategori)
        spCabang = findViewById(R.id.spCabang)
    }

    private fun setupDropdown() {
        val kategoriList = arrayOf("Makanan", "Minuman", "Snack", "Lainnya")
        val cabangList = arrayOf("Cabang A", "Cabang B", "Cabang C")

        spKategori.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, kategoriList)
        )

        spCabang.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cabangList)
        )
    }

    private fun setupListener() {

        cbUnlimited.setOnCheckedChangeListener { _, isChecked ->
            etStok.isEnabled = !isChecked
            if (isChecked) etStok.setText("0")
        }

        btnCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }

        btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        }

        btnSimpan.setOnClickListener {
            saveProduk()
        }
    }

    // CAMERA RESULT
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                ivPreview.setImageBitmap(bitmap)
            }
        }

    // GALLERY RESULT
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                ivPreview.setImageURI(imageUri)
            }
        }

    private fun saveProduk() {

        val nama = etNama.text.toString()
        val hargaText = etHarga.text.toString()
        val stokText = if (cbUnlimited.isChecked) "0" else etStok.text.toString()

        val kategori = spKategori.text.toString()
        val cabang = spCabang.text.toString()

        if (nama.isEmpty() || hargaText.isEmpty() || kategori.isEmpty() || cabang.isEmpty()) {
            Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        val harga = hargaText.toIntOrNull() ?: 0
        val stok = stokText.toIntOrNull() ?: 0

        val idProduk = myRef.push().key ?: return

        val image = imageUri?.toString() ?: ""

        val produk = ModelProduk(
            idProduk = idProduk,
            namaProduk = nama,
            hargaProduk = harga,
            idKategori = kategori,
            idCabang = cabang,
            fotoProduk = image,
            stokProduk = stok,
            statusProduk = "Aktif",
            createdAt = System.currentTimeMillis().toString(),
            updateAt = System.currentTimeMillis().toString()
        )

        myRef.child(idProduk).setValue(produk)
            .addOnSuccessListener {

                Toast.makeText(this, "Produk berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()

            }
            .addOnFailureListener {

                Toast.makeText(this, "Gagal simpan produk", Toast.LENGTH_SHORT).show()
            }
    }
}
