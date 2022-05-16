package com.example.fundoonotes.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.R
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.LoginViewModel
import com.example.fundoonotes.viewmodel.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

private const val TAG = "Login"
class Login : Fragment(R.layout.fragment_login) {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var etLoginEmail : EditText
    private lateinit var etLoginPassword : EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var tvForgotPass: TextView
    private lateinit var btnGSignIn: SignInButton
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etLoginEmail = requireView().findViewById(R.id.etForgotEmail)
        etLoginPassword = requireView().findViewById(R.id.etLoginPassword)
        btnLogin = requireView().findViewById(R.id.btnLogin)
        tvRegister = requireView().findViewById(R.id.tvRegister)
        tvForgotPass = requireView().findViewById(R.id.tvForgotPassword)
        btnGSignIn = requireView().findViewById(R.id.btnGSignIn)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService()))[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestGoogleSignIn()

        resultLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account: GoogleSignInAccount = task.result
                    val e = task.exception
                    firebaseAuthWithGoogle(account.idToken)
                }catch (e: ApiException){
                    Log.e(TAG, "Couldn't find account. Error: " + e.message)
                }
            }
        }
    }

    private fun requestGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("859629026284-n5kq152s5dr4qecljpr9li2ieiou9jk0.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.context as Activity, gso)
    }


    override fun onStart() {
        super.onStart()

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

        tvForgotPass.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, ForgotPassword())
                addToBackStack(null)
                commit()
            }
        }

        btnGSignIn.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
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
            loginViewModel.userLogin(email, password)
            loginViewModel.loginStatus.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                    startActivity(intentNoteHome)
                    requireActivity().finish()
                }else{
                    Toast.makeText(this.context, it.message , Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val intentNoteHome = Intent(this.context, MainActivity::class.java)
        if (idToken != null) {
            loginViewModel.userGLogin(idToken)
            loginViewModel.loginStatus.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                    startActivity(intentNoteHome)
                    requireActivity().finish()
                }else{
                    Toast.makeText(this.context, it.message , Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Log.e(TAG, "Couldn't find ID Token")
        }
    }
}