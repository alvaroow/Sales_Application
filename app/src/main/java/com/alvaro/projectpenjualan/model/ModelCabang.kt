package com.alvaro.projectpenjualan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelCabang(

    var idCabang: String? = null,

    var namaCabang: String? = null,

    var alamatCabang: String? = null,

    var telpCabang: String? = null,

    var statusCabang: String? = "Aktif"

) : Parcelable