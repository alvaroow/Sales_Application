package com.alvaro.projectpenjualan.model

data class ModelCart(
    var produk: ModelProduk = ModelProduk(),
    var qty: Int = 0
)