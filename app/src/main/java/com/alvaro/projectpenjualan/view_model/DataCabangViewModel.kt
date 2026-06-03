package com.alvaro.projectpenjualan.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataCabangViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("cabang")

    val cabangList = MutableLiveData<ArrayList<ModelCabang>>()
    private var originalCabangList = ArrayList<ModelCabang>()

    val isLoading = MutableLiveData<Boolean>()
    val isSearchEmpty = MutableLiveData<Boolean>()

    init {
        getData()
    }

    fun getData() {
        isLoading.value = true

        myRef.orderByChild("idCabang").limitToLast(100)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isLoading.value = false

                    val list = ArrayList<ModelCabang>()

                    for (dataSnapshot in snapshot.children) {
                        dataSnapshot.getValue(ModelCabang::class.java)?.let {
                            list.add(it)
                        }
                    }

                    originalCabangList = list
                    cabangList.value = list
                    isSearchEmpty.value = list.isEmpty()
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading.value = false
                }
            })
    }


    fun toggleStatus(cabang: ModelCabang) {
        val id = cabang.idCabang ?: return

        val newStatus =
            if (cabang.statusCabang == "Aktif") "Non Aktif"
            else "Aktif"

        // update Firebase
        myRef.child(id).child("statusCabang").setValue(newStatus)

        // update local biar langsung berubah di UI
        cabang.statusCabang = newStatus
        cabangList.value = originalCabangList
    }


    fun filterList(query: String?) {
        if (query.isNullOrEmpty()) {
            cabangList.value = originalCabangList
        } else {
            val filtered = originalCabangList.filter {
                it.namaCabang?.contains(query, true) == true
            }
            cabangList.value = ArrayList(filtered)
        }
    }
}