package com.alvaro.projectpenjualan.adapter

import android.content.res.ColorStateList
import android.graphics.Color
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
    private var longListener: OnItemLongClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) { this.listener = listener }
    fun setOnStatusClickListener(listener: OnStatusClickListener) { this.statusListener = listener }
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) { this.longListener = listener }

    interface OnItemClickListener { fun onItemClick(cabang: ModelCabang) }
    interface OnStatusClickListener { fun onStatusClick(cabang: ModelCabang) }
    interface OnItemLongClickListener { fun onItemLongClick(cabang: ModelCabang) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data_cabang, parent, false)
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

            if (status == "Non Aktif") {
                chipStatus.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FEE2E2"))
                chipStatus.setTextColor(Color.parseColor("#DC2626"))
            } else {
                chipStatus.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#DCFCE7"))
                chipStatus.setTextColor(Color.parseColor("#16A34A"))
            }

            // Klik Biasa (Untuk Edit)
            itemView.setOnClickListener { listener?.onItemClick(item) }

            // Tekan Tahan (Untuk Hapus)
            itemView.setOnLongClickListener {
                longListener?.onItemLongClick(item)
                true
            }

            // Klik Status
            chipStatus.setOnClickListener { statusListener?.onStatusClick(item) }
        }
    }
}