package com.alvaro.projectpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.model.ModelCabang

class AdapterCabang(

    private val list:
    List<ModelCabang>

)
    :
    RecyclerView.Adapter<
            AdapterCabang.ViewHolder>() {

    class ViewHolder(
        itemView: View
    )
        :
        RecyclerView.ViewHolder(
            itemView
        ){

        val tvNama =

            itemView.findViewById<TextView>(
                R.id.tvNamaCabang
            )

        val tvAlamat =

            itemView.findViewById<TextView>(
                R.id.tvAlamatCabang
            )

        val tvStatus =

            itemView.findViewById<TextView>(
                R.id.tvStatusCabang
            )

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =

            LayoutInflater
                .from(
                    parent.context
                )

                .inflate(
                    R.layout.item_data_cabang,
                    parent,
                    false
                )

        return ViewHolder(
            view
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item =
            list[position]

        holder.tvNama.text =
            item.namaCabang

        holder.tvAlamat.text =
            item.alamatCabang

        holder.tvStatus.text =
            item.statusCabang

    }

}