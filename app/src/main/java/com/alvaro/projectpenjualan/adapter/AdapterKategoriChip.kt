package com.alvaro.projectpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelKategori

class AdapterKategoriChip(
    private val list: List<ModelKategori>,
    private val onClick: (ModelKategori) -> Unit
) : RecyclerView.Adapter<AdapterKategoriChip.VH>() {

    private var selectedPosition = 0

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaKategori)
        val cv: CardView = itemView.findViewById(R.id.cvKategoriChip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kategori_chip, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.tvNama.text = item.namaKategori

        if (selectedPosition == position) {
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.primary))
            holder.tvNama.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
        } else {
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.bg_surface))
            holder.tvNama.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.text_main))
        }

        holder.itemView.setOnClickListener {
            val oldPos = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(oldPos)
            notifyItemChanged(selectedPosition)
            onClick(item)
        }
    }
}