package com.example.crimebis.config

import com.google.gson.annotations.SerializedName


data class Responses(
    @field:SerializedName("data")
    val data: List<CrimesItems>
)

data class ResponseLogin(
    @field:SerializedName("data")
    val data: loginResult
)


data class loginResult(
    @field:SerializedName("username")
    val username: String,
)

data class CrimesItems(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("namaBis")
    val namaBis: String,

    @field:SerializedName("namaSupir")
    val namaSupir: String,

    @field:SerializedName("tujuanBis")
    val tujuanBis: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("tanggalkejadian")
    val tanggalkejadian: String,

    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("lon")
    val lon: Double
)