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
import java.text.NumberFormat
import java.util.Locale

class AdapterProduk(
    private val produkList: List<ModelProduk>
) : RecyclerView.Adapter<AdapterProduk.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(produk: ModelProduk)
    }

    interface OnStatusClickListener {
        fun onStatusClick(produk: ModelProduk)
    }


    interface OnItemLongClickListener {
        fun onItemLongClick(produk: ModelProduk)
    }

    private var listener: OnItemClickListener? = null
    private var statusListener: OnStatusClickListener? = null
    private var longListener: OnItemLongClickListener? = null

    fun setOnItemClickListener(l: OnItemClickListener) { listener = l }
    fun setOnStatusClickListener(l: OnStatusClickListener) { statusListener = l }
    fun setOnItemLongClickListener(l: OnItemLongClickListener) { longListener = l }

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
            
            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            tvHarga.text = formatRupiah.format(p.hargaProduk ?: 0)
                .replace("Rp", "Rp ")
                .replace(",00", "")

            if (p.stokProduk == 0) {
                tvStok.text = "Stok: Tak Terbatas"
            } else {
                tvStok.text = "Stok: ${p.stokProduk}"
            }

            tvKategori.text = "Kategori: ${p.idKategori}"
            tvCabang.text = "Cabang: ${p.idCabang}"


            val status = p.statusProduk ?: "Aktif"
            chipStatus.text = status

            // Logika Warna Status
            if (status == "Non Aktif") {
                chipStatus.setChipBackgroundColorResource(R.color.red)
            } else {
                chipStatus.setChipBackgroundColorResource(R.color.green)
            }

            // Klik biasa (Untuk Edit)
            itemView.setOnClickListener {
                listener?.onItemClick(p)
            }

            //  Tekan tahan (Untuk Hapus)
            itemView.setOnLongClickListener {
                longListener?.onItemLongClick(p)
                true
            }

            // Klik status
            chipStatus.setOnClickListener {
                statusListener?.onStatusClick(p)
            }
        }
    }
}