package com.example.fundoonotes

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

private const val TAG = "Register"

class Register : Fragment(R.layout.fragment_register) {

    private lateinit var btnBack : ImageView
    private lateinit var etRegEmail : EditText
    private lateinit var etRegPassword : EditText
    private lateinit var btnRegister: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var userFName: EditText
    private lateinit var userLName: EditText
    private lateinit var db : FirebaseFirestore
    private lateinit var userId : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = requireView().findViewById(R.id.btnBack)
        etRegEmail = requireView().findViewById(R.id.etRegEmail)
        etRegPassword = requireView().findViewById(R.id.etRegPassword)
        btnRegister = requireView().findViewById(R.id.btnRegister)
        userFName = requireView().findViewById(R.id.etRegFName)
        userLName = requireView().findViewById(R.id.etRegLName)
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        btnRegister.setOnClickListener {
            createUser()
        }

        btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun createUser() {
        val intentUserLogin = Intent(this.context, Authenticate::class.java)

        if(TextUtils.isEmpty(etRegEmail.toString())){
            etRegEmail.error = "Email can't be empty"
            etRegEmail.requestFocus()
        }else if(TextUtils.isEmpty(etRegPassword.toString())){
            etRegPassword.error = "Password can't be empty"
            etRegPassword.requestFocus()
        }else {
            auth.createUserWithEmailAndPassword(etRegEmail.text.toString().trim(), etRegPassword.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    userId = auth.currentUser?.uid.toString()
                    val docReference: DocumentReference = db.collection("users").document(userId)
                    Toast.makeText(this.context, "Registration Successful. Login to proceed.", Toast.LENGTH_LONG).show()

                    val userDetails: HashMap<String, String> = HashMap<String, String>()
                    userDetails["fName"] = userFName.text.toString().trim()
                    userDetails["lName"] = userLName.text.toString().trim()
                    userDetails["eMail"] = etRegEmail.text.toString().trim()

                    docReference.set(userDetails).addOnSuccessListener {
                        Log.d(TAG, "User profile created for: $userId")
                    }

                    startActivity(intentUserLogin)
                }else{
                    Log.e(TAG, "Registration Error: ${it.exception.toString()}")
                    Toast.makeText(this.context, "Registration error" + it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}