package com.alvaro.projectpenjualan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.adapter.AdapterCart
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCart : BottomSheetDialogFragment() {

    private lateinit var rv: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var adapter: AdapterCart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.activity_bottom_sheet_cart, container, false)

        rv = view.findViewById(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvTotal)

        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = AdapterCart(
            getData = { CartManager.getAll() },
            onIncrease = {
                CartManager.increase(it)
                refresh()
            },
            onDecrease = {
                CartManager.decrease(it)
                refresh()
            },
            onRemove = {
                CartManager.remove(it)
                refresh()
            }
        )

        rv.adapter = adapter
        refresh()

        return view
    }

    private fun refresh() {
        tvTotal.text = "Total: Rp ${CartManager.getTotal()}"
        rv.adapter?.notifyDataSetChanged()
    }
}