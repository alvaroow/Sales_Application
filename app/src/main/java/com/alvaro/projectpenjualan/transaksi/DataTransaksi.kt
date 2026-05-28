package com.alvaro.projectpenjualan.transaksi

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.CartManager
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterTransaksi
import com.alvaro.projectpenjualan.transaksi.BottomSheetCart
import com.alvaro.projectpenjualan.model.ModelProduk
import com.google.firebase.database.*

class DataTransaksi : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: AdapterTransaksi
    private val list = ArrayList<ModelProduk>()

    private lateinit var ref: DatabaseReference

    private lateinit var miniCart: View
    private lateinit var tvMiniInfo: TextView

    private var bottomSheet: BottomSheetCart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_transaksi)

        rv = findViewById(R.id.rvTransaksi)
        miniCart = findViewById(R.id.mini_bottom_sheet)
        tvMiniInfo = miniCart.findViewById(R.id.tvMiniInfo)
        miniCart.findViewById<View>(R.id.btnOpenCart).setOnClickListener {
            openCart()
        }

        rv.layoutManager = LinearLayoutManager(this)

        adapter = AdapterTransaksi(list) { produk ->
            CartManager.add(produk)
            openCart()
        }

        rv.adapter = adapter

        // ✅ FIX PENTING: INIT DULU
        ref = FirebaseDatabase.getInstance().getReference("produk")

        CartManager.setOnCartChanged {
            runOnUiThread {
                updateMiniCart()
            }
        }

        loadData()
        updateMiniCart()
    }

    private fun openCart() {
        if (bottomSheet == null) {
            bottomSheet = BottomSheetCart()
        }

        if (bottomSheet?.isAdded == true) return

        bottomSheet?.show(supportFragmentManager, "cart")
    }

    private fun updateMiniCart() {
        if (!::tvMiniInfo.isInitialized || !::miniCart.isInitialized) return

        val totalItem = CartManager.getAll().size
        val totalHarga = CartManager.getTotal()

        if (totalItem == 0) {
            miniCart.visibility = View.GONE
        } else {
            miniCart.visibility = View.VISIBLE
            tvMiniInfo.text = "$totalItem item | Rp $totalHarga"
        }
    }

    private fun loadData() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                list.clear()

                for (data in snapshot.children) {
                    val produk = data.getValue(ModelProduk::class.java)

                    // ✅ FIX 2: Produk yang Non Aktif otomatis disembunyikan dari kasir
                    produk?.let {
                        val status = it.statusProduk ?: "Aktif"
                        if (status == "Aktif") {
                            list.add(it)
                        }
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}