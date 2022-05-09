package com.example.fundoonotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class Login : Fragment(R.layout.fragment_login) {

    private lateinit var etLoginEmail : EditText
    private lateinit var etLoginPassword : EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etLoginEmail = requireView().findViewById(R.id.etLoginEmail)
        etLoginPassword = requireView().findViewById(R.id.etLoginPassword)
        btnLogin = requireView().findViewById(R.id.btnLogin)
        tvRegister = requireView().findViewById(R.id.tvRegister)
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener{
            loginUser()
        }

        tvRegister.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, Register())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun loginUser() {
        val email : String = etLoginEmail.text.toString().trim()
        val password : String = etLoginPassword.text.toString().trim()
        val intentNoteHome = Intent(this.context, MainActivity::class.java)

        if(TextUtils.isEmpty(email)){
            etLoginEmail.error = "Email can't be empty"
            etLoginEmail.requestFocus()
        }else if(TextUtils.isEmpty(password)){
            etLoginPassword.error = "Password can't be empty"
            etLoginPassword.requestFocus()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this.context, "Login Successful!", Toast.LENGTH_LONG).show()
                    startActivity(intentNoteHome)
                    requireActivity().finish()
                }else{
                    Toast.makeText(this.context, "Login error" + it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}