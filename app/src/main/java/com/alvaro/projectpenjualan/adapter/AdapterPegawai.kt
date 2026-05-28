package com.alvaro.projectpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelPegawai
import com.google.android.material.chip.Chip

class AdapterPegawai(private val list: List<ModelPegawai>) :
    RecyclerView.Adapter<AdapterPegawai.ViewHolder>() {

    private var listener: OnItemClickListener? = null
    private var longListener: OnItemLongClickListener? = null
    private var statusListener: OnStatusClickListener? = null

    fun setOnItemClickListener(l: OnItemClickListener) { listener = l }
    fun setOnItemLongClickListener(l: OnItemLongClickListener) { longListener = l }
    fun setOnStatusClickListener(l: OnStatusClickListener) { statusListener = l }

    interface OnItemClickListener { fun onItemClick(pegawai: ModelPegawai) }
    interface OnItemLongClickListener { fun onItemLongClick(pegawai: ModelPegawai) }
    interface OnStatusClickListener { fun onStatusClick(pegawai: ModelPegawai) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // SESUAIKAN NAMA XML INI DENGAN MILIKMU
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data_pegawai, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // SESUAIKAN ID INI DENGAN XML MILIKMU
        private val tvNama = itemView.findViewById<TextView>(R.id.tvNamaPegawai)
        private val tvTelp = itemView.findViewById<TextView>(R.id.tvTelpPegawai)
        private val chipStatus = itemView.findViewById<Chip>(R.id.chipStatusPegawai)

        fun bind(p: ModelPegawai) {
            tvNama.text = p.namaPegawai
            tvTelp.text = p.noTelp
            val status = p.statusPegawai ?: "Aktif"
            chipStatus.text = status

            if (status == "Non Aktif") {
                chipStatus.setChipBackgroundColorResource(R.color.red)
            } else {
                chipStatus.setChipBackgroundColorResource(R.color.green)
            }

            itemView.setOnClickListener { listener?.onItemClick(p) }
            itemView.setOnLongClickListener { longListener?.onItemLongClick(p); true }
            chipStatus.setOnClickListener { statusListener?.onStatusClick(p) }
        }
    }
}