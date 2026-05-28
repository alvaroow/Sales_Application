package com.alvaro.projectpenjualan.transaksi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.projectpenjualan.CartManager
import com.alvaro.projectpenjualan.R
import com.alvaro.projectpenjualan.adapter.AdapterCart
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class BottomSheetCart : BottomSheetDialogFragment() {

    private lateinit var rv: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: MaterialButton
    private lateinit var adapter: AdapterCart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.activity_bottom_sheet_cart, container, false)

        rv = view.findViewById(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        btnCheckout.setOnClickListener {

            startActivity(
                Intent(requireContext(), CheckoutActivity::class.java)
            )
        }
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = AdapterCart(
            CartManager.getAll(),
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
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }
}