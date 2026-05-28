package com.alvaro.projectpenjualan

import com.alvaro.projectpenjualan.model.ModelCart
import com.alvaro.projectpenjualan.model.ModelProduk

object CartManager {

    private val list = mutableListOf<ModelCart>()
    private var listener: (() -> Unit)? = null

    fun setOnCartChanged(l: () -> Unit) {
        listener = l
    }

    private fun notifyChange() {
        listener?.invoke()
    }

    fun add(p: ModelProduk) {
        val item = list.find { it.produk.idProduk == p.idProduk }
        if (item == null) {
            list.add(ModelCart(p, 1))
        } else {
            item.qty++
        }
        notifyChange()
    }

    fun increase(p: ModelProduk) {
        list.find { it.produk.idProduk == p.idProduk }?.let {
            it.qty++
        }
        notifyChange()
    }

    fun decrease(p: ModelProduk) {
        list.find { it.produk.idProduk == p.idProduk }?.let {
            it.qty--
            if (it.qty <= 0) list.remove(it)
        }
        notifyChange()
    }

    fun remove(p: ModelProduk) {
        list.removeAll { it.produk.idProduk == p.idProduk }
        notifyChange()
    }

    fun getAll() = list

    fun getTotal(): Int {
        return list.sumOf { it.qty * (it.produk.hargaProduk ?: 0) }
    }
}