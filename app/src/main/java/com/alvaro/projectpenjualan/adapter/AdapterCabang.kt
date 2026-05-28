package com.alvaro.projectpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelCabang
import com.google.android.material.chip.Chip

class AdapterCabang(private val list: List<ModelCabang>) :
    RecyclerView.Adapter<AdapterCabang.ViewHolder>() {

    private var listener: OnItemClickListener? = null
    private var statusListener: OnStatusClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setOnStatusClickListener(listener: OnStatusClickListener) {
        this.statusListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(cabang: ModelCabang)
    }

    interface OnStatusClickListener {
        fun onStatusClick(cabang: ModelCabang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_cabang, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNama = itemView.findViewById<TextView>(R.id.tvNamaCabang)
        private val tvAlamat = itemView.findViewById<TextView>(R.id.tvAlamatCabang)
        private val chipStatus = itemView.findViewById<Chip>(R.id.tvStatusCabang)

        fun bind(item: ModelCabang) {
            tvNama.text = item.namaCabang
            tvAlamat.text = item.alamatCabang

            val status = item.statusCabang ?: "Aktif"
            chipStatus.text = status

            // Warna status disamakan persis dengan Kategori
            if (status == "Non Aktif") {
                chipStatus.setChipBackgroundColorResource(R.color.red)
            } else {
                chipStatus.setChipBackgroundColorResource(R.color.green)
            }

            // Klik Card
            itemView.setOnClickListener {
                listener?.onItemClick(item)
            }

            // Klik Status
            chipStatus.setOnClickListener {
                statusListener?.onStatusClick(item)
            }
        }
    }
}