package com.example.fundoonotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class Authenticate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticate)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, Login())
            commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}