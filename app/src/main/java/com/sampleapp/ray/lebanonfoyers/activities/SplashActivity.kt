package com.sampleapp.ray.lebanonfoyers.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sampleapp.ray.lebanonfoyers.R
import com.sampleapp.ray.lebanonfoyers.activities.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadingScreen()
    }

    private fun loadingScreen() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val mainIntent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 1500)
    }
}
