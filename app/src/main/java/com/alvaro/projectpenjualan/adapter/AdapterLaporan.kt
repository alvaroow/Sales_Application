package com.alvaro.projectpenjualan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelTransaksi
import com.alvaro.projectpenjualan.transaksi.StrukActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterLaporan(
    private val list: List<ModelTransaksi>
) : RecyclerView.Adapter<AdapterLaporan.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvIdTransaksi)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggalTransaksi)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotalTransaksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_laporan, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]

        holder.tvId.text = item.idTransaksi
        holder.tvTotal.text = "Rp ${item.total}"

        holder.tvTanggal.text = SimpleDateFormat(
            "dd-MM-yyyy HH:mm",
            Locale.getDefault()
        ).format(Date(item.tanggal))

        // Kalau diklik, lemparkan ID Transaksi ke StrukActivity!
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, StrukActivity::class.java)
            intent.putExtra("idTransaksi", item.idTransaksi)
            holder.itemView.context.startActivity(intent)
        }
    }
}