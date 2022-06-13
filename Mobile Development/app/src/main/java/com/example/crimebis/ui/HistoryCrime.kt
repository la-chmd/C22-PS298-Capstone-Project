package com.example.crimebis.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crimebis.R
import com.example.crimebis.adapter.ListCrimeAdapter
import com.example.crimebis.config.ApiConfig
import com.example.crimebis.config.Responses
import com.example.crimebis.model.Crime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryCrime : AppCompatActivity() {
    private val list = ArrayList<Crime>()
    private lateinit var rvFragment: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_crime)
        supportActionBar?.hide()
        progressBar = findViewById(R.id.progressBar)
        rvFragment = findViewById(R.id.rv_fragment)
        rvFragment.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(this.context)
            // set the custom adapter to the RecyclerView
        }
        getCrime()
    }
    private fun getCrime() {
        showLoading(true)
        val client = ApiConfig.getApiService().getCrime()
        client.enqueue(object : Callback<Responses> {
            override fun onResponse(
                call: Call<Responses>,
                response: Response<Responses>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.data
                    if (responseBody != null) {
                        val listCrime = ArrayList<Crime>()
                        for (i in responseBody.indices) {
                            val id = responseBody[i].id
                            val namaBis = responseBody[i].namaBis
                            val namaSupir = responseBody[i].namaSupir
                            val tujuanBis = responseBody[i].tujuanBis
                            val image = responseBody[i].image
                            val tanggalKejadian = responseBody[i].tanggalkejadian
                            val lat = responseBody[i].lat
                            val lon = responseBody[i].lon
                            val users = Crime(id, namaBis, namaSupir, tujuanBis, image, tanggalKejadian, lat, lon)
                            listCrime.add(users)
                        }
                        showLoading(false)
                        list.addAll(listCrime)
                        showRecyclerList()
                    }
                } else {
                    Log.e("TAG", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<Responses>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.message}")
            }
        })
    }

    private fun showRecyclerList() {

        val listCrimeAdapter = ListCrimeAdapter(list)
        rvFragment.adapter = listCrimeAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}