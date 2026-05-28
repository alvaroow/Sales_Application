package com.alvaro.projectpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelCart
import com.alvaro.projectpenjualan.model.ModelProduk

class AdapterCart(
    private val list: List<ModelCart>,
    private val onIncrease: (ModelProduk) -> Unit,
    private val onDecrease: (ModelProduk) -> Unit,
    private val onRemove: (ModelProduk) -> Unit
) : RecyclerView.Adapter<AdapterCart.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNama = itemView.findViewById<TextView>(R.id.tvNamaCart)
        private val tvHarga = itemView.findViewById<TextView>(R.id.tvHargaCart)
        private val tvQty = itemView.findViewById<TextView>(R.id.tvQty)

        private val btnPlus = itemView.findViewById<TextView>(R.id.btnPlus)
        private val btnMinus = itemView.findViewById<TextView>(R.id.btnMinus)
        private val btnDelete = itemView.findViewById<TextView>(R.id.btnDelete)

        fun bind(item: ModelCart) {

            tvNama.text = item.produk.namaProduk
            tvHarga.text = "Rp ${item.produk.hargaProduk}"
            tvQty.text = "Qty: ${item.qty}"

            btnPlus.setOnClickListener {
                onIncrease(item.produk)
            }

            btnMinus.setOnClickListener {
                onDecrease(item.produk)
            }

            btnDelete.setOnClickListener {
                onRemove(item.produk)
            }
        }
    }
}