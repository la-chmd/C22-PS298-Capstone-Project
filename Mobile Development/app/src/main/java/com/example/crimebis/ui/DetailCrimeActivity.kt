package com.example.crimebis.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.crimebis.R
import com.example.crimebis.databinding.ActivityDetailCrimeBinding
import com.example.crimebis.databinding.ActivityMainBinding
import com.example.crimebis.model.Crime

class DetailCrimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCrimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCrimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val image =  intent.getStringExtra("image")
        val namaBis =  intent.getStringExtra("namaBis")
        val tujuanBis =  intent.getStringExtra("tujuanBis")
        val tanggal =  intent.getStringExtra("tanggalKejadian")
        val namaSupir =  intent.getStringExtra("namaSupir")
        val lat =  intent.getStringExtra("lat")
        val lon =  intent.getStringExtra("lon")

        Glide.with(applicationContext)
            .load("https://backendCapstone.ridhodzaki.repl.co/image/${image}")
            .into(binding.imageCctv)
        binding.txNamaBis.text = namaBis
        binding.txTanggalKejadian.text = tanggal
        binding.txTujuanBis.text = tujuanBis
        binding.txnamaSupir.text = namaSupir

    }
}