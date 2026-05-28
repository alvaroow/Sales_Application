package com.alvaro.projectpenjualan.model

data class ModelCart(
    var produk: ModelProduk = ModelProduk(), // <-- Ini tambahannya
    var qty: Int = 0
)