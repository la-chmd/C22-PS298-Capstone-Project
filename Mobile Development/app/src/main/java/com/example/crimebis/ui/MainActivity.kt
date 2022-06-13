package com.example.crimebis.ui

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.crimebis.R
import com.example.crimebis.config.ApiConfig
import com.example.crimebis.config.GeofenceBroadcastReceiver
import com.example.crimebis.config.Responses
import com.example.crimebis.databinding.ActivityMainBinding
import com.example.crimebis.model.Crime
import com.example.crimebis.model.UserPreference
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreferences: UserPreference
    private lateinit var geofencingClient: GeofencingClient
    private val geofenceRadius = 300.0
    private var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        userPreferences = UserPreference(this)
        val c = Calendar.getInstance()
        val year: Int
        val month: Int
        val day: Int
        year = c[Calendar.YEAR]
        month = c[Calendar.MONTH]
        day = c[Calendar.DATE]
        date = "${day.toString()}/${month + 1}/$year"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.elevation = 0f

        var isLogin = userPreferences.getLogin()

        if (isLogin) {
            binding.textButton.text = "Logout"
        } else {
            binding.textButton.text = "Login"
        }

        binding.textButton.setOnClickListener {
            if (isLogin) {
                userPreferences.logout()
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryCrime::class.java))
        }
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = GeofenceBroadcastReceiver.ACTION_GEOFENCE_EVENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        getMyLocation()
        getCrime(date)
    }

    private fun getCrime(date: String) {
        val client = ApiConfig.getApiService().getCrimeWithTanggal(date)
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
//                            val users = Crime(id, namaBis, namaSupir, tujuanBis, image, tanggalKejadian, lat, lon)
//                            listCrime.add(users)
                            val stanford = LatLng(lat, lon)
                            mMap.addMarker(MarkerOptions().position(stanford).title("$namaBis").snippet("$tujuanBis"))
                            mMap.addCircle(
                                CircleOptions()
                                    .center(stanford)
                                    .radius(geofenceRadius)
                                    .fillColor(0x22FF0000)
                                    .strokeColor(Color.RED)
                                    .strokeWidth(3f)
                            )
                            addGeofence(lat.toDouble(), lon.toDouble())
                        }
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


    private val requestBackgroundLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }


    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q


    @TargetApi(Build.VERSION_CODES.Q)
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (runningQOrLater) {
                    requestBackgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    getMyLocation()
                }
            }
        }


    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    @TargetApi(Build.VERSION_CODES.Q)
    private fun checkForegroundAndBackgroundLocationPermission(): Boolean {
        val foregroundLocationApproved = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }


    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkForegroundAndBackgroundLocationPermission()) {
            mMap.isMyLocationEnabled = true
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    @SuppressLint("MissingPermission")
    private fun addGeofence(lat: Double, lon: Double) {
        geofencingClient = LocationServices.getGeofencingClient(this)


        val geofence = Geofence.Builder()
            .setRequestId("kampus")
            .setCircularRegion(
                lat,
                lon,
                geofenceRadius.toFloat()
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER)
            .setLoiteringDelay(5000)
            .build()


        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()


        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnCompleteListener {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                    addOnSuccessListener {
                        showToast("Geofencing added")
                    }
                    addOnFailureListener {
                        showToast("Geofencing not added : ${it.message}")
                    }
                }
            }
        }
    }


    private fun showToast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }
}