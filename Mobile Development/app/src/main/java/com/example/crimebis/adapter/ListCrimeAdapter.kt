package com.example.crimebis.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crimebis.R
import com.example.crimebis.databinding.CrimeItemBinding
import com.example.crimebis.model.Crime
import com.example.crimebis.model.UserPreference
import com.example.crimebis.ui.DetailCrimeActivity

class ListCrimeAdapter (private val listCrime: ArrayList<Crime>) : RecyclerView.Adapter<ListCrimeAdapter.ListViewHolder>() {

    private lateinit var userPreferences: UserPreference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.crime_item, parent, false)
        userPreferences = UserPreference(parent.context)
//        Log.e("login", "${userPreferences.getLogin()}")
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listCrime[position])
    }

    override fun getItemCount(): Int = listCrime.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageCctv: ImageView = itemView.findViewById(R.id.imageCctv)
        private var txnamaBis: TextView = itemView.findViewById(R.id.txNamaBis)
        private var txTujuanBis: TextView = itemView.findViewById(R.id.txTujuanBis)
        private var txTanggal: TextView = itemView.findViewById(R.id.txTanggalKejadian)

        fun bind(crime: Crime) {
            Log.e("crime", "$crime")
            Glide.with(itemView.context)
                .load("https://backendCapstone.ridhodzaki.repl.co/image/${crime.image}")
                .into(imageCctv)
            txnamaBis.text = crime.namaBis
            txTujuanBis.text = crime.tujuanBis
            txTanggal.text = crime.tanggalkejadian

            itemView.setOnClickListener {
                var userPreference = UserPreference(itemView.context)
                if (userPreference.getLogin()) {
                    val intent = Intent(itemView.context, DetailCrimeActivity::class.java)
                    intent.putExtra("id", crime.image)
                    intent.putExtra("namaBis", crime.namaBis)
                    intent.putExtra("tujuanBis", crime.tujuanBis)
                    intent.putExtra("namaSupir", crime.namaSupir)
                    intent.putExtra("tanggalKejadian", crime.tanggalkejadian)
                    intent.putExtra("image", crime.image)
                    intent.putExtra("lat", crime.lat)
                    intent.putExtra("lon", crime.lon)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(imageCctv, "image"),
                            Pair(txnamaBis, "nama"),
                            Pair(txTujuanBis, "tujuan"),
                            Pair(txTanggal, "tanggal"),
                        )

                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }

            }
        }
    }


}