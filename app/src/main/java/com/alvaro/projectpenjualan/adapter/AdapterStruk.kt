package com.alvaro.projectpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelCart
import java.text.NumberFormat
import java.util.Locale

class AdapterStruk(
    private val list: List<ModelCart>
) : RecyclerView.Adapter<AdapterStruk.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Sesuaikan dengan ID di item_struk_produk.xml
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaProduk)
        val tvQty: TextView = itemView.findViewById(R.id.tvQty)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_struk_produk, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]

        holder.tvNama.text = item.produk.namaProduk
        holder.tvQty.text = "${item.qty}x"

        // Kalkulasi subtotal
        val subtotal = item.qty * (item.produk.hargaProduk ?: 0)
        
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        holder.tvSubtotal.text = formatRupiah.format(subtotal)
            .replace("Rp", "Rp ")
            .replace(",00", "")
    }
}