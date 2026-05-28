package com.alvaro.projectpenjualan.model

data class ModelTransaksi(
    var idTransaksi: String = "",
    var total: Int = 0,
    var bayar: Int = 0,
    var kembalian: Int = 0,
    var tanggal: Long = 0,
    var items: List<ModelCart> = emptyList()
)