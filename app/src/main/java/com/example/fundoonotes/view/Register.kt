package com.example.fundoonotes.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.RegisterViewModel
import com.example.fundoonotes.viewmodel.RegisterViewModelFactory
import androidx.lifecycle.Observer
import com.example.fundoonotes.R


private const val TAG = "Register"

class Register : Fragment(R.layout.fragment_register) {

    private lateinit var btnBack : ImageView
    private lateinit var etRegEmail : EditText
    private lateinit var etRegPassword : EditText
    private lateinit var btnRegister: Button
    private lateinit var userFName: EditText
    private lateinit var userLName: EditText

    private lateinit var registerViewModel: RegisterViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = requireView().findViewById(R.id.btnBack)
        etRegEmail = requireView().findViewById(R.id.etRegEmail)
        etRegPassword = requireView().findViewById(R.id.etRegPassword)
        btnRegister = requireView().findViewById(R.id.btnRegister)
        userFName = requireView().findViewById(R.id.etRegFName)
        userLName = requireView().findViewById(R.id.etRegLName)

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(UserAuthService()))[RegisterViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        btnRegister.setOnClickListener {
            createUser()
        }

        btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun createUser() {
        val email : String = etRegEmail.text.toString().trim()
        val password : String = etRegPassword.text.toString().trim()
        val fName: String = userFName.text.toString().trim()
        val lName: String = userLName.text.toString().trim()

        val intentUserLogin = Intent(this.context, MainActivity::class.java)

        if(TextUtils.isEmpty(etRegEmail.toString())){
            etRegEmail.error = "Email can't be empty"
            etRegEmail.requestFocus()
        }else if(TextUtils.isEmpty(etRegPassword.toString())){
            etRegPassword.error = "Password can't be empty"
            etRegPassword.requestFocus()
        }else {
            registerViewModel.userRegister(fName, lName, email, password)
            registerViewModel.registerStatus.observe(viewLifecycleOwner, Observer{
                if(it.status){
                    Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                    startActivity(intentUserLogin)
                }else{
                    Toast.makeText(this.context, it.message , Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}