package com.alvaro.projectpenjualan.produk

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.projectpenjualan.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
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
    private lateinit var spKategori: MaterialAutoCompleteTextView
    private lateinit var spCabang: MaterialAutoCompleteTextView

    private var imageUri: Uri? = null

    private val db = FirebaseDatabase.getInstance()
    private val produkRef = db.getReference("produk")
    private val kategoriRef = db.getReference("kategori")
    private val cabangRef = db.getReference("cabang")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_produk)

        initView()
        setupListener()
        setupDropdown()
        loadKategori()
        loadCabang()
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

    // ================= DROPDOWN =================

    private fun setupDropdown() {
        spKategori.setOnClickListener { spKategori.showDropDown() }
        spCabang.setOnClickListener { spCabang.showDropDown() }

        spKategori.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) spKategori.showDropDown()
        }

        spCabang.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) spCabang.showDropDown()
        }
    }

// ================= FIREBASE KATEGORI =================

    private fun loadKategori() {
        kategoriRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<String>()

                for (data in snapshot.children) {
                    val nama = data.child("namaKategori").getValue(String::class.java)
                    // Ambil statusnya, kalau kosong anggap saja Aktif
                    val status = data.child("statusKategori").getValue(String::class.java) ?: "Aktif"

                    // Filter: Hanya tambahkan ke dropdown jika statusnya Aktif
                    if (nama != null && status == "Aktif") {
                        list.add(nama)
                    }
                }

                val adapter = ArrayAdapter(
                    this@ModProduk,
                    android.R.layout.simple_dropdown_item_1line,
                    list
                )

                spKategori.setAdapter(adapter)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // ================= FIREBASE CABANG =================

    private fun loadCabang() {
        cabangRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<String>()

                for (data in snapshot.children) {
                    val nama = data.child("namaCabang").getValue(String::class.java)
                    // Ambil statusnya, kalau kosong anggap saja Aktif
                    val status = data.child("statusCabang").getValue(String::class.java) ?: "Aktif"

                    // Filter: Hanya tambahkan ke dropdown jika statusnya Aktif
                    if (nama != null && status == "Aktif") {
                        list.add(nama)
                    }
                }

                val adapter = ArrayAdapter(
                    this@ModProduk,
                    android.R.layout.simple_dropdown_item_1line,
                    list
                )

                spCabang.setAdapter(adapter)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // ================= LISTENER =================

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

    // ================= CAMERA =================

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                ivPreview.setImageBitmap(bitmap)
            }
        }

    // ================= GALLERY =================

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                ivPreview.setImageURI(imageUri)
            }
        }

    // ================= SAVE =================

    private fun saveProduk() {

        val nama = etNama.text.toString()
        val harga = etHarga.text.toString()
        val stok = if (cbUnlimited.isChecked) "0" else etStok.text.toString()

        val kategori = spKategori.text.toString()
        val cabang = spCabang.text.toString()

        if (nama.isEmpty() || harga.isEmpty() || kategori.isEmpty() || cabang.isEmpty()) {
            Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        val id = produkRef.push().key ?: return

        val produk = ModelProduk(
            idProduk = id,
            namaProduk = nama,
            hargaProduk = harga.toInt(),
            idKategori = kategori,
            idCabang = cabang,
            fotoProduk = imageUri?.toString() ?: "",
            stokProduk = stok.toInt(),
            statusProduk = "Aktif",
            createdAt = System.currentTimeMillis().toString(),
            updateAt = System.currentTimeMillis().toString()
        )

        produkRef.child(id).setValue(produk)
            .addOnSuccessListener {
                Toast.makeText(this, "Produk berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal simpan produk", Toast.LENGTH_SHORT).show()
            }
    }
}