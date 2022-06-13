package com.example.crimebis.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.crimebis.config.ApiConfig
import com.example.crimebis.config.ResponseLogin
import com.example.crimebis.databinding.ActivityLoginBinding
import com.example.crimebis.model.User
import com.example.crimebis.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreferences: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userPreferences = UserPreference(this)

        binding.loginBtn.setOnClickListener {
            var username = binding.edUsername.text.toString()
            var password = binding.edPassword.text.toString()

            when {
                username.isEmpty() -> {
                    binding.edUsername.error = "Masukkan Username"
                }
                password.isEmpty() -> {
                    binding.edPassword.error = "Masukkan password"
                }
                else -> {
//                    val jsonParams: Map<String, Any> = ArrayMap()
                    val params: MutableMap<String, String> = HashMap()
                    params["user"] = "YOUR USERNAME"
                    params["pass"] = "YOUR PASSWORD"
                    val client = ApiConfig.getApiService().login(params)
                    client.enqueue(object : Callback<ResponseLogin> {
                        override fun onResponse(
                            call: Call<ResponseLogin>,
                            response: Response<ResponseLogin>
                        ) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()?.data
                                val usename = responseBody?.username
                                userPreferences.setUser(User(usename.toString(), true))
                                goDashboard()
                            } else {
                                Log.e("TAG", "onFailure: ${response.message()}")
                            }
                        }
                        override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                            Log.e("TAG", "onFailure: ${t.message}")
                        }
                    })

                }
            }
        }
    }

    private fun goDashboard() {
        startActivity(Intent(this, MainActivity::class.java))
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}