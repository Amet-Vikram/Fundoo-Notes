package com.example.fundoonotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogout : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intentUserLogin = Intent(this, Authenticate::class.java )

        auth = Firebase.auth
        btnLogout = findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener{
            auth.signOut()
            startActivity(intentUserLogin)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val intentUserLogin = Intent(this, Authenticate::class.java )

        if(currentUser == null){
            startActivity(intentUserLogin)
        }
    }
}