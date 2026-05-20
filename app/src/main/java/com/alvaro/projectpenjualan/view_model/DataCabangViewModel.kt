package com.alvaro.projectpenjualan.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class DataCabangViewModel : ViewModel() {

    private val db =
        FirebaseDatabase
            .getInstance()
            .getReference("cabang")

    val cabangList =
        MutableLiveData<
                ArrayList<ModelCabang>
                >()

    init {
        getData()
    }

    private fun getData() {

        db.addValueEventListener(

            object : ValueEventListener {

                override fun onDataChange(
                    snapshot: DataSnapshot
                ) {

                    val list =
                        ArrayList<ModelCabang>()

                    for(data in snapshot.children){

                        val cabang =
                            data.getValue(
                                ModelCabang::class.java
                            )

                        cabang?.let {
                            list.add(it)
                        }
                    }

                    cabangList.value =
                        list
                }

                override fun onCancelled(
                    error: DatabaseError
                ) {}

            }

        )

    }

}