package com.example.fundoonotes.view.ui

import android.os.Bundle
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
import com.example.fundoonotes.model.Validator


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

        btnBack = requireView().findViewById(R.id.ivBackToLogin)
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
            val validator = Validator()
            val email : String = etRegEmail.text.toString().trim()
            val password : String = etRegPassword.text.toString().trim()
            val fName: String = userFName.text.toString().trim()
            val lName: String = userLName.text.toString().trim()

            when{
                !validator.validateName(fName) -> {
                    userFName.error = "Invalid Name"
                    userFName.requestFocus()
                }
                !validator.validateName(lName) -> {
                    userLName.error = "Invalid Name"
                    userLName.requestFocus()
                }
                !validator.validateEmail(email) -> {
                    etRegEmail.error = "Invalid Email"
                    etRegEmail.requestFocus()
                }
                !validator.validatePassword(password) -> {
                    etRegPassword.error = "Invalid Password"
                    etRegPassword.requestFocus()
                }
                else -> {
                    createUser(fName, lName, email, password)
                    Toast.makeText(this.context, "Registering!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun createUser(fName: String, lName: String, email: String, password: String) {

        registerViewModel.userRegister(fName, lName, email, password)
        registerViewModel.registerStatus.observe(viewLifecycleOwner, Observer{
            if(it.status){
                Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()

                //User Will be taken to Login Fragment
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, Login())
                    addToBackStack(null)
                    commit()
                }
            }else{
                Toast.makeText(this.context, it.message , Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun validateUserInput(fName: String, lName: String, email: String, password: String): Boolean{
        val validator = Validator()
        if(validator.validateName(fName) && validator.validateName(lName) && validator.validateEmail(email) && validator.validatePassword(password)){
            return true
        }
        return false
    }
}