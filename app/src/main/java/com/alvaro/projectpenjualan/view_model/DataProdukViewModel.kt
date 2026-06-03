package com.alvaro.projectpenjualan.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DataProdukViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()

    private val myRef =
        database.getReference("produk")

    val produkList = MutableLiveData<ArrayList<ModelProduk>>()
    private var originalList = ArrayList<ModelProduk>()

    init {
        getData()
    }

    private fun getData() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<ModelProduk>()
                for (data in snapshot.children) {
                    val produk = data.getValue(ModelProduk::class.java)
                    if (produk != null) {
                        list.add(produk)
                    }
                }
                originalList = list
                produkList.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    fun searchProduk(query: String) {
        if (query.isEmpty()) {
            produkList.value = originalList
        } else {
            val filteredList = originalList.filter {
                it.namaProduk?.contains(query, ignoreCase = true) == true
            }
            produkList.value = ArrayList(filteredList)
        }
    }

    fun toggleStatus(
        produk: ModelProduk
    ) {

        val statusBaru =

            if (
                produk.statusProduk ==
                "Aktif"
            )

                "Non Aktif"

            else

                "Aktif"

        produk.idProduk?.let {

            myRef.child(it)
                .child("statusProduk")
                .setValue(statusBaru)

        }
    }
}