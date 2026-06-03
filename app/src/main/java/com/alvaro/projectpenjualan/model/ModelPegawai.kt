package com.alvaro.projectpenjualan.model

data class ModelPegawai(
    val idPegawai: String? = null,
    val namaPegawai: String? = null,
    val noTelp: String? = null,
    val password: String? = null,
    var statusPegawai: String? = "Aktif"
)