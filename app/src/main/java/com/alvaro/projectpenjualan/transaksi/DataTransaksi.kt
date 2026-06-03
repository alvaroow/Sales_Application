package com.alvaro.projectpenjualan.transaksi

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.CartManager
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterKategoriChip
import com.alvaro.projectpenjualan.adapter.AdapterTransaksi
import com.alvaro.projectpenjualan.model.ModelProduk
import com.alvaro.projectpenjualan.model.ModelKategori
import com.google.firebase.database.*

class DataTransaksi : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var rvKategori: RecyclerView
    private lateinit var searchView: SearchView
    
    private lateinit var adapter: AdapterTransaksi
    private lateinit var adapterKategori: AdapterKategoriChip
    
    private val list = ArrayList<ModelProduk>()
    private val fullList = ArrayList<ModelProduk>()
    private val kategoriList = ArrayList<ModelKategori>()

    private lateinit var ref: DatabaseReference
    private lateinit var refKategori: DatabaseReference

    private lateinit var miniCart: View
    private lateinit var tvMiniInfo: TextView
    private lateinit var tvMiniTotal: TextView

    private var bottomSheet: BottomSheetCart? = null
    
    private var selectedKategori: String = "Semua"
    private var searchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_transaksi)

        rv = findViewById(R.id.rvTransaksi)
        rvKategori = findViewById(R.id.rvKategori)
        searchView = findViewById(R.id.searchView)
        
        miniCart = findViewById(R.id.mini_bottom_sheet)
        tvMiniInfo = miniCart.findViewById(R.id.tvMiniInfo)
        tvMiniTotal = miniCart.findViewById(R.id.tvMiniTotal)
        
        miniCart.findViewById<View>(R.id.btnOpenCart).setOnClickListener {
            openCart()
        }

        // Use GridLayoutManager for 2 columns
        rv.layoutManager = GridLayoutManager(this, 2)
        adapter = AdapterTransaksi(list) { produk ->
            CartManager.add(produk)
            updateMiniCart() // Ensure mini cart updates when adding
        }
        rv.adapter = adapter

        // Setup Kategori
        rvKategori.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterKategori = AdapterKategoriChip(kategoriList) { kategori ->
            selectedKategori = kategori.namaKategori ?: "Semua"
            filterData()
        }
        rvKategori.adapter = adapterKategori

        ref = FirebaseDatabase.getInstance().getReference("produk")
        refKategori = FirebaseDatabase.getInstance().getReference("kategori")

        CartManager.setOnCartChanged {
            runOnUiThread {
                updateMiniCart()
            }
        }

        setupSearch()
        loadKategori()
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
        if (!::tvMiniInfo.isInitialized || !::tvMiniTotal.isInitialized || !::miniCart.isInitialized) return

        val totalItem = CartManager.getAll().size
        val totalHarga = CartManager.getTotal()

        if (totalItem == 0) {
            miniCart.visibility = View.GONE
        } else {
            miniCart.visibility = View.VISIBLE
            
            val formatRupiah = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("in", "ID"))
            val formattedHarga = formatRupiah.format(totalHarga).replace("Rp", "Rp ").replace(",00", "")
            
            tvMiniInfo.text = "$totalItem Item"
            tvMiniTotal.text = formattedHarga
        }
    }

    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText ?: ""
                filterData()
                return true
            }
        })
    }

    private fun loadKategori() {
        refKategori.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                kategoriList.clear()
                kategoriList.add(ModelKategori(idKategori = "all", namaKategori = "Semua", statusKategori = "Aktif"))
                
                for (data in snapshot.children) {
                    val kat = data.getValue(ModelKategori::class.java)
                    if (kat != null && kat.statusKategori == "Aktif") {
                        kategoriList.add(kat)
                    }
                }
                adapterKategori.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun filterData() {
        list.clear()
        for (produk in fullList) {
            val matchesKategori = selectedKategori == "Semua" || produk.idKategori == selectedKategori
            val matchesSearch = produk.namaProduk?.contains(searchQuery, ignoreCase = true) == true
            
            if (matchesKategori && matchesSearch) {
                list.add(produk)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun loadData() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fullList.clear()
                for (data in snapshot.children) {
                    val produk = data.getValue(ModelProduk::class.java)
                    produk?.let {
                        val status = it.statusProduk ?: "Aktif"
                        if (status == "Aktif") {
                            fullList.add(it)
                        }
                    }
                }
                filterData()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
