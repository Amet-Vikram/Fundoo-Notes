package com.example.fundoonotes.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fundoonotes.R
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "ForgotPassword"

class ForgotPassword : Fragment(R.layout.fragment_forgot_password) {

    private lateinit var ivBack: ImageView
    private lateinit var etEmail: EditText
    private lateinit var btnForgot: Button
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivBack = requireView().findViewById(R.id.ivBackToLogin)
        etEmail = requireView().findViewById(R.id.etForgotEmail)
        btnForgot = requireView().findViewById(R.id.btnVerification)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        btnForgot.setOnClickListener{
            val email = etEmail.text.toString().trim()
            Log.i(TAG, "Forgot clicked!")
            if(email.isEmpty()){
                etEmail.error = "Can't be left empty."
            }else{
                Log.i(TAG, "current user found!")
                forgetPassword(email)
            }
        }
    }

    private fun forgetPassword(email: String) {
        Log.i(TAG, "Inside ForgetPassword")
        auth.sendPasswordResetEmail(email).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this.context, "Reset email sent.", Toast.LENGTH_LONG).show()

                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, Login())
                    addToBackStack(null)
                    commit()
                }
            }else{
                Toast.makeText(this.context, "Couldn't verify.", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error : " + it.exception!!.message)
            }
        }
    }
}