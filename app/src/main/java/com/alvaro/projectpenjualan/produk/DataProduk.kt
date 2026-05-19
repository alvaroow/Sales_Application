package com.alvaro.projectpenjualan.produk

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterProduk
import com.alvaro.projectpenjualan.model.DataProdukViewModel
import com.alvaro.projectpenjualan.model.ModelProduk
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DataProduk : AppCompatActivity() {

    private val viewModel: DataProdukViewModel by viewModels()

    private lateinit var rvDATAPRODUK: RecyclerView
    private lateinit var fabDATAPRODUKTambah:
            FloatingActionButton

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_data_produk
        )

        init()

        rvDATAPRODUK.layoutManager =
            LinearLayoutManager(
                this
            )

        rvDATAPRODUK
            .setHasFixedSize(
                true
            )

        viewModel.produkList
            .observe(this) { list ->

                val adapter =
                    AdapterProduk(
                        list
                    )

                rvDATAPRODUK.adapter =
                    adapter

                // klik item

                adapter
                    .setOnItemClickListener(

                        object :
                            AdapterProduk
                            .OnItemClickListener {

                            override fun onItemClick(
                                produk: ModelProduk
                            ) {

                                Toast.makeText(
                                    this@DataProduk,
                                    produk.namaProduk,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }
                    )

                // klik status

                adapter
                    .setOnStatusClickListener(

                        object :
                            AdapterProduk
                            .OnStatusClickListener {

                            override fun onStatusClick(
                                produk: ModelProduk
                            ) {

                                viewModel
                                    .toggleStatus(
                                        produk
                                    )

                            }

                        }

                    )

            }

    }

    private fun init() {

        rvDATAPRODUK =
            findViewById(
                R.id.rvDATA_PRODUK
            )

        fabDATAPRODUKTambah =
            findViewById(
                R.id.fabDATA_PRODUK_Tambah
            )

        fabDATAPRODUKTambah
            .setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        ModProduk::class.java
                    )

                )

            }

    }

}