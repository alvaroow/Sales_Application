package com.alvaro.projectpenjualan.transaksi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.CartManager
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterCart
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BottomSheetCart : BottomSheetDialogFragment() {

    private lateinit var rv: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: MaterialButton
    private lateinit var spKasir: MaterialAutoCompleteTextView // ✅ Variabel Kasir
    private lateinit var adapter: AdapterCart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.activity_bottom_sheet_cart, container, false)

        rv = view.findViewById(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)
        spKasir = view.findViewById(R.id.spKasir) // ✅ Inisialisasi

        // ✅ Panggil Data Kasir dari Firebase
        loadKasir()

        // Setup aksi tombol Checkout
        btnCheckout.setOnClickListener {
            val namaKasir = spKasir.text.toString()

            // Cegah lanjut kalau kasir belum dipilih
            if (namaKasir.isEmpty()) {
                Toast.makeText(requireContext(), "Pilih kasir terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cegah lanjut kalau keranjang kosong
            if (CartManager.getAll().isEmpty()) {
                Toast.makeText(requireContext(), "Keranjang masih kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            // 👇 INI KUNCI UTAMANYA BIAR NAMANYA TERKIRIM
            intent.putExtra("NAMA_KASIR", namaKasir)
            startActivity(intent)

            dismiss() // Tutup bottom sheet
        }

        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = AdapterCart(
            CartManager.getAll(),
            onIncrease = {
                CartManager.increase(it)
                refresh()
            },
            onDecrease = {
                CartManager.decrease(it)
                refresh()
            },
            onRemove = {
                CartManager.remove(it)
                refresh()
            }
        )

        rv.adapter = adapter
        refresh()

        return view
    }

    private fun loadKasir() {
        spKasir.setOnClickListener { spKasir.showDropDown() }
        spKasir.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) spKasir.showDropDown() }

        FirebaseDatabase.getInstance().getReference("pegawai")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listKasir = ArrayList<String>()
                    for (data in snapshot.children) {
                        val nama = data.child("namaPegawai").getValue(String::class.java)
                        val status = data.child("statusPegawai").getValue(String::class.java) ?: "Aktif"

                        // Hanya masukkan ke dropdown jika statusnya Aktif
                        if (nama != null && status == "Aktif") {
                            listKasir.add(nama)
                        }
                    }

                    if (context != null) {
                        val adapterKasir = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listKasir)
                        spKasir.setAdapter(adapterKasir)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun refresh() {
        tvTotal.text = "Total: Rp ${CartManager.getTotal()}"
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }
}