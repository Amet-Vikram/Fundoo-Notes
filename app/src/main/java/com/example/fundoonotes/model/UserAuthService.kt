package com.example.fundoonotes.model

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "UserAuthService"

class UserAuthService {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userId : String
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun userLogin(email: String, password: String, listener: (AuthListener) -> Unit){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
//                Toast.makeText(this.context, "Login Successful!", Toast.LENGTH_LONG).show()
//                startActivity(intentNoteHome)
//                requireActivity().finish()
                listener(AuthListener(true, "User login successful."))
            }else{
//                Toast.makeText(this.context, "Login error" + it.exception.toString(), Toast.LENGTH_LONG).show()
                listener(AuthListener(false, "User login failed."))
            }
        }
    }

    fun userRegister(
        fName: String,
        lName: String,
        email: String,
        password: String,
        listener: (AuthListener) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                userId = auth.currentUser?.uid.toString()

                //Firebase Storage
                val docReference: DocumentReference = db.collection("users").document(userId)
                val userDetails: HashMap<String, String> = HashMap<String, String>()
                userDetails["fName"] = fName
                userDetails["lName"] = lName
                userDetails["eMail"] = email

                docReference.set(userDetails).addOnSuccessListener {
                    Log.d(TAG, "User profile created for: $userId")
                }

                listener(AuthListener(true, "User registration successful."))

            }else{
                listener(AuthListener(false, "User registration failed."))
            }
        }
    }
}