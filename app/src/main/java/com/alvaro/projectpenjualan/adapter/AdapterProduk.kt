package com.alvaro.projectpenjualan.adapter

import android.content.Context
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
) : RecyclerView.Adapter<AdapterProduk.ProdukViewHolder>() {

    lateinit var appContext: Context

    interface OnItemClickListener {
        fun onItemClick(produk: ModelProduk)
    }

    interface OnStatusClickListener {
        fun onStatusClick(produk: ModelProduk)
    }

    private var listener: OnItemClickListener? = null
    private var statusListener: OnStatusClickListener? = null

    fun setOnItemClickListener(
        listener: OnItemClickListener
    ) {
        this.listener = listener
    }

    fun setOnStatusClickListener(
        listener: OnStatusClickListener
    ) {
        statusListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProdukViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_data_produk,
                parent,
                false
            )

        appContext = parent.context

        return ProdukViewHolder(view)
    }

    override fun getItemCount(): Int {
        return produkList.size
    }

    override fun onBindViewHolder(
        holder: ProdukViewHolder,
        position: Int
    ) {
        holder.bind(
            produkList[position]
        )
    }

    inner class ProdukViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        // SESUAI XML BARU

        private val tvNamaProduk =
            itemView.findViewById<TextView>(
                R.id.tvNamaProduk
            )

        private val tvHargaProduk =
            itemView.findViewById<TextView>(
                R.id.tvHargaProduk
            )

        private val tvStokProduk =
            itemView.findViewById<TextView>(
                R.id.tvStokProduk
            )

        private val chipStatusProduk =
            itemView.findViewById<Chip>(
                R.id.chipStatusProduk
            )

        private val ivProduk =
            itemView.findViewById<ImageView>(
                R.id.ivProduk
            )

        fun bind(
            produk: ModelProduk
        ) {

            tvNamaProduk.text =
                produk.namaProduk

            tvHargaProduk.text =
                "Rp ${produk.hargaProduk}"

            tvStokProduk.text =
                "Stok: ${produk.stokProduk}"

            val status =
                produk.statusProduk
                    ?: "Aktif"

            chipStatusProduk.text =
                status

            if (
                status == "Aktif"
            ) {

                chipStatusProduk
                    .setChipBackgroundColorResource(
                        R.color.green
                    )

            } else {

                chipStatusProduk
                    .setChipBackgroundColorResource(
                        R.color.red
                    )
            }

            itemView.setOnClickListener {

                listener?.onItemClick(
                    produk
                )
            }

            chipStatusProduk
                .setOnClickListener {

                    statusListener
                        ?.onStatusClick(
                            produk
                        )
                }
        }
    }
}