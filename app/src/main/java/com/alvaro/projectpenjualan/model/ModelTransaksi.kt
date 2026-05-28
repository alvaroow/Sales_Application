package com.alvaro.projectpenjualan.model

data class ModelTransaksi(
    val idTransaksi: String = "",
    val total: Int = 0,
    val bayar: Int = 0,
    val kembalian: Int = 0,
    val tanggal: Long = 0L,
    val items: List<ModelCart> = emptyList(),
    val namaKasir: String = ""
)