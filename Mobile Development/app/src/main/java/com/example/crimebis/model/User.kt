package com.example.crimebis.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User (
    var username : String? = null,
    var isLogin : Boolean = false,
) : Parcelable