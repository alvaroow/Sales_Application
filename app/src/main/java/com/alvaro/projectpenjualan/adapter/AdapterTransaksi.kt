package com.alvaro.projectpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelProduk

class AdapterTransaksi(
    private val list: List<ModelProduk>,
    private val onClick: (ModelProduk) -> Unit
) : RecyclerView.Adapter<AdapterTransaksi.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_produk, parent, false)

        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        val item = list[position]

        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount() = list.size

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNama =
            itemView.findViewById<TextView>(R.id.tvNamaProduk)

        private val tvHarga =
            itemView.findViewById<TextView>(R.id.tvHargaProduk)

        private val tvKategori =
            itemView.findViewById<TextView>(R.id.tvKategori)

        private val tvCabang =
            itemView.findViewById<TextView>(R.id.tvCabang)

        private val tvStok =
            itemView.findViewById<TextView>(R.id.tvStokProduk)

        fun bind(p: ModelProduk) {

            tvNama.text = p.namaProduk

            tvHarga.text = "Rp ${p.hargaProduk}"

            tvKategori.text = "Kategori: ${p.idKategori}"

            tvCabang.text = "Cabang: ${p.idCabang}"

            tvStok.text = "Stok: ${p.stokProduk}"
        }
    }
}