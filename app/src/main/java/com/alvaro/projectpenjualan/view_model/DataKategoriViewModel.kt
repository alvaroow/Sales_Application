package com.alvaro.projectpenjualan.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataKategoriViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("kategori")

    val kategoriList = MutableLiveData<ArrayList<ModelKategori>>()
    private var originalKategoriList = ArrayList<ModelKategori>()

    val isLoading = MutableLiveData<Boolean>()
    val isSearchEmpty = MutableLiveData<Boolean>()

    init {
        getData()
    }

    fun getData() {
        isLoading.value = true

        myRef.orderByChild("idKategori").limitToLast(100)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isLoading.value = false

                    val list = ArrayList<ModelKategori>()

                    for (dataSnapshot in snapshot.children) {
                        dataSnapshot.getValue(ModelKategori::class.java)?.let {
                            list.add(it)
                        }
                    }

                    originalKategoriList = list
                    kategoriList.value = list
                    isSearchEmpty.value = list.isEmpty()
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading.value = false
                }
            })
    }

    // ✅ TOGGLE STATUS (INI INTI FITUR)
    fun toggleStatus(kategori: ModelKategori) {
        val id = kategori.idKategori ?: return

        val newStatus =
            if (kategori.statusKategori == "Aktif") "Non Aktif"
            else "Aktif"

        // update Firebase
        myRef.child(id).child("statusKategori").setValue(newStatus)

        // update local biar langsung berubah di UI
        kategori.statusKategori = newStatus
        kategoriList.value = originalKategoriList
    }

    fun filterList(query: String?) {
        if (query.isNullOrEmpty()) {
            kategoriList.value = originalKategoriList
        } else {
            val filtered = originalKategoriList.filter {
                it.namaKategori?.contains(query, true) == true
            }
            kategoriList.value = ArrayList(filtered)
        }
    }
}