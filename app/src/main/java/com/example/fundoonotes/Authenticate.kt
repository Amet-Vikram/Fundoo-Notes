package com.example.fundoonotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Authenticate : AppCompatActivity() {

    private lateinit var etLoginEmail : EditText
    private lateinit var etLoginPassword : EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticate)

        etLoginEmail = findViewById(R.id.etLoginEmail)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

//        val intentUserRegister = Intent(this, Register::class.java )

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener{
            loginUser()
        }

        tvRegister.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, Register())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun loginUser() {
        val email : String = etLoginEmail.toString()
        val password : String = etLoginPassword.toString()
        val intentNoteHome = Intent(this, MainActivity::class.java)

        if(TextUtils.isEmpty(email)){
            etLoginEmail.error = "Email can't be empty"
            etLoginEmail.requestFocus()
        }else if(TextUtils.isEmpty(password)){
            etLoginPassword.error = "Password can't be empty"
            etLoginPassword.requestFocus()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_LONG).show()
                    startActivity(intentNoteHome)
                }else{
                    Toast.makeText(this, "Login error" + it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}