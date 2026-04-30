package com.alvaro.projectpenjualan.adapter

import android.content.Context
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

    lateinit var appContext: Context

    interface OnItemClickListener {
        fun onItemClick(kategori: ModelKategori)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_data_kategori, parent, false)
        appContext = parent.context
        return KategoriViewHolder(view)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        val kategori = kategoriList[position]
        holder.bind(kategori)
    }

    override fun getItemCount(): Int = kategoriList.size

    inner class KategoriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaKategori: TextView = itemView.findViewById(R.id.tvCARD_KATEGORI_Nama)
        val chipStatus: Chip = itemView.findViewById(R.id.chipAdd)

        fun bind(kategori: ModelKategori) {
            tvNamaKategori.text = kategori.namaKategori
            val status = kategori.statusKategori

            itemView.setOnClickListener {
                listener?.onItemClick(kategori)
            }
        }
    }
}