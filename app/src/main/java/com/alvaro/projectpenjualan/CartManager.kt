package com.alvaro.projectpenjualan

import com.alvaro.projectpenjualan.model.ModelCart
import com.alvaro.projectpenjualan.model.ModelProduk

object CartManager {

    private val cartList = mutableListOf<ModelCart>()

    private var onCartChanged: (() -> Unit)? = null

    fun setOnCartChanged(listener: () -> Unit) {
        onCartChanged = listener
    }

    fun getAll() = cartList

    fun add(product: ModelProduk) {
        val existing = cartList.find { it.produk.idProduk == product.idProduk }

        if (existing != null) {
            existing.qty++
        } else {
            cartList.add(ModelCart(product, 1))
        }

        onCartChanged?.invoke()
    }

    fun increase(product: ModelProduk) {
        cartList.find { it.produk.idProduk == product.idProduk }?.let {
            it.qty++
            onCartChanged?.invoke()
        }
    }

    fun decrease(product: ModelProduk) {
        val item = cartList.find { it.produk.idProduk == product.idProduk }
        if (item != null) {
            item.qty--
            if (item.qty <= 0) cartList.remove(item)
            onCartChanged?.invoke()
        }
    }

    fun remove(product: ModelProduk) {
        cartList.removeAll { it.produk.idProduk == product.idProduk }
        onCartChanged?.invoke()
    }

    fun getTotal(): Int {
        return cartList.sumOf {
            (it.produk.hargaProduk ?: 0) * it.qty
        }
    }

    fun clear() {
        cartList.clear()
        onCartChanged?.invoke()
    }
}