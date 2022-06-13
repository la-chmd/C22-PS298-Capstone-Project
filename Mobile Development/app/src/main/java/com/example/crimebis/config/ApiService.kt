package com.example.crimebis.config

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("crime/getCrime")
    fun getCrime(
    ): Call<Responses>

    @GET("crime/getCrime/{tanggal}")
    fun getCrimeWithTanggal(
        @Path("tanggal") tanggal: String
    ): Call<Responses>

    @POST("user/login")
    fun login(
        @Body body: MutableMap<String, String>
    ): Call<ResponseLogin>
}