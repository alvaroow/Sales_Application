package com.alvaro.projectpenjualan.model

data class ModelCart(
    val produk: ModelProduk,
    var qty: Int = 1
)