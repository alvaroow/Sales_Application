package com.alvaro.projectpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelProduk
import com.google.android.material.chip.Chip

class AdapterProduk(
    private val produkList: List<ModelProduk>
) : RecyclerView.Adapter<AdapterProduk.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(produk: ModelProduk)
    }

    interface OnStatusClickListener {
        fun onStatusClick(produk: ModelProduk)
    }

    private var listener: OnItemClickListener? = null
    private var statusListener: OnStatusClickListener? = null

    fun setOnItemClickListener(l: OnItemClickListener) {
        listener = l
    }

    fun setOnStatusClickListener(l: OnStatusClickListener) {
        statusListener = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_produk, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = produkList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(produkList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNama = itemView.findViewById<TextView>(R.id.tvNamaProduk)
        private val tvHarga = itemView.findViewById<TextView>(R.id.tvHargaProduk)
        private val tvStok = itemView.findViewById<TextView>(R.id.tvStokProduk)
        private val chipStatus = itemView.findViewById<Chip>(R.id.chipStatusProduk)
        private val ivProduk = itemView.findViewById<ImageView>(R.id.ivProduk)

        private val tvKategori = itemView.findViewById<TextView>(R.id.tvKategori)
        private val tvCabang = itemView.findViewById<TextView>(R.id.tvCabang)

        fun bind(p: ModelProduk) {

            tvNama.text = p.namaProduk
            tvHarga.text = "Rp ${p.hargaProduk}"
            tvStok.text = "Stok: ${p.stokProduk}"

            tvKategori.text = "Kategori: ${p.idKategori}"
            tvCabang.text = "Cabang: ${p.idCabang}"

            chipStatus.text = p.statusProduk ?: "Aktif"

            itemView.setOnClickListener {
                listener?.onItemClick(p)
            }

            chipStatus.setOnClickListener {
                statusListener?.onStatusClick(p)
            }
        }
    }
}