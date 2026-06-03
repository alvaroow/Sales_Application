package com.alvaro.projectpenjualan.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelProduk
import com.google.android.material.chip.Chip
import java.text.NumberFormat
import java.util.Locale

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

        private val chipStatus =
            itemView.findViewById<Chip>(R.id.chipStatusProduk)

        fun bind(p: ModelProduk) {

            tvNama.text = p.namaProduk

            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            tvHarga.text = formatRupiah.format(p.hargaProduk ?: 0)
                .replace("Rp", "Rp ")
                .replace(",00", "")

            tvKategori.text = "Kategori: ${p.idKategori}"

            tvCabang.text = "Cabang: ${p.idCabang}"

            tvStok.text = "Stok: ${p.stokProduk}"

            val status = p.statusProduk ?: "Aktif"
            chipStatus.text = status

            if (status == "Non Aktif") {
                chipStatus.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FEE2E2"))
                chipStatus.setTextColor(Color.parseColor("#DC2626"))
            } else {
                chipStatus.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#DCFCE7"))
                chipStatus.setTextColor(Color.parseColor("#16A34A"))
            }
        }
    }
}