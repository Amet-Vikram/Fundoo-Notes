package com.example.fundoonotes

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class Register : Fragment(R.layout.fragment_register) {

    private lateinit var btnBack : ImageView
    private lateinit var etRegEmail : EditText
    private lateinit var etRegPassword : EditText
    private lateinit var btnRegister: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = requireView().findViewById(R.id.btnBack)
        etRegEmail = requireView().findViewById(R.id.etRegEmail)
        etRegPassword = requireView().findViewById(R.id.etRegPassword)
        btnRegister = requireView().findViewById(R.id.btnRegister)
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            createUser()
        }

        btnBack.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun createUser() {
        val email : String = etRegEmail.toString()
        val password : String = etRegPassword.toString()
        val intentUserLogin = Intent(this.context, Authenticate::class.java)

        if(TextUtils.isEmpty(email)){
            etRegEmail.error = "Email can't be empty"
            etRegEmail.requestFocus()
        }else if(TextUtils.isEmpty(password)){
            etRegPassword.error = "Password can't be empty"
            etRegPassword.requestFocus()
        }else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this.context, "Registration Successful. Login to proceed.", Toast.LENGTH_LONG).show()
                    startActivity(intentUserLogin)
                }else{
                    Toast.makeText(this.context, "Registration error" + it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}