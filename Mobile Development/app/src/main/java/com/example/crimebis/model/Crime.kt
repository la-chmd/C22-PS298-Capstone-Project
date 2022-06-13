package com.example.crimebis.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Crime (
    var id : Int,
    var namaBis : String,
    var namaSupir : String,
    var tujuanBis : String,
    var image : String,
    var tanggalkejadian : String,
    var lat : Double? = 0.0,
    var lon : Double? = 0.0
) : Parcelable