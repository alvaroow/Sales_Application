package com.alvaro.projectpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelKategori
import com.google.android.material.chip.Chip

class AdapterKategori(private val kategoriList: List<ModelKategori>) :
    RecyclerView.Adapter<AdapterKategori.KategoriViewHolder>() {

    private var listener: OnItemClickListener? = null
    private var statusListener: OnStatusClickListener? = null

    // ✅ Tambahan: Kabel untuk menangkap Tekan Tahan (Hapus)
    private var longListener: OnItemLongClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setOnStatusClickListener(listener: OnStatusClickListener) {
        this.statusListener = listener
    }

    // ✅ Tambahan: Setter untuk Tekan Tahan
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.longListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(kategori: ModelKategori)
    }

    interface OnStatusClickListener {
        fun onStatusClick(kategori: ModelKategori)
    }

    // ✅ Tambahan: Interface untuk Tekan Tahan
    interface OnItemLongClickListener {
        fun onItemLongClick(kategori: ModelKategori)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_kategori, parent, false)
        return KategoriViewHolder(view)
    }

    override fun getItemCount(): Int = kategoriList.size

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        holder.bind(kategoriList[position])
    }

    inner class KategoriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNamaKategori: TextView =
            itemView.findViewById(R.id.tvCARD_KATEGORI_Nama)

        private val chipStatus: Chip =
            itemView.findViewById(R.id.chipAdd)

        fun bind(kategori: ModelKategori) {

            tvNamaKategori.text = kategori.namaKategori
            val status = kategori.statusKategori ?: "Aktif"

            chipStatus.text = status

            // warna status
            if (status == "Non Aktif") {
                chipStatus.setChipBackgroundColorResource(R.color.red)
            } else {
                chipStatus.setChipBackgroundColorResource(R.color.green)
            }

            // klik card (Untuk Edit)
            itemView.setOnClickListener {
                listener?.onItemClick(kategori)
            }

            // ✅ Tambahan: Tekan tahan card (Untuk Hapus)
            itemView.setOnLongClickListener {
                longListener?.onItemLongClick(kategori)
                true // Wajib return true agar klik biasa tidak ikut terpanggil
            }

            // klik status (Untuk Ubah Aktif/Non Aktif)
            chipStatus.setOnClickListener {
                statusListener?.onStatusClick(kategori)
            }
        }
    }
}