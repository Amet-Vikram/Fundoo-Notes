package com.example.fundoonotes.model


import android.util.Log

import com.example.fundoonotes.R
import com.example.fundoonotes.api.Constant
import com.example.fundoonotes.api.LoginListener
import com.example.fundoonotes.api.LoginLoader
import com.example.fundoonotes.api.LoginResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "UserAuthService"

class UserAuthService() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userId : String
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun userLogin(email: String, password: String, listener: (AuthListener) -> Unit){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                listener(AuthListener(true, "User login successful."))
            }else{
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
            if (it.isSuccessful) {
                userId = auth.currentUser?.uid.toString()

                //Firebase Storage
                val docReference: DocumentReference = db.collection("users").document(userId)
                val userDetails: HashMap<String, String> = HashMap()
                userDetails["fName"] = fName
                userDetails["lName"] = lName
                userDetails["eMail"] = email

                docReference.set(userDetails).addOnSuccessListener {
                    Log.d(TAG, "User profile created for: $userId")
                }

                listener(AuthListener(true, "User registration successful."))

            } else {
                listener(AuthListener(false, "User registration failed."))
            }
        }
    }


    fun loadUserData(user: (User) -> Unit) {
        userId = auth.currentUser?.uid.toString()
        val userDetails = db.collection("users").document(userId)
        var currentUser : User?
        Log.i(TAG, "Load user data")
        userDetails.get().addOnSuccessListener {
            if(it.exists()){
                Log.i(TAG, "Fetched User Details")
                val fullName: String = it.getString("fName") + " " + it.getString("lName")
                val email: String = it.getString("eMail").toString()
                currentUser = User(userId,fullName, email)
                user(currentUser!!)
            }
            else{
                Log.e(TAG, "Failed to get User Details")
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String?, listener: (AuthListener) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener() {
            if(it.isSuccessful){
                listener(AuthListener(true, "User login successful."))
            }else{
                listener(AuthListener(false, "User login failed."))
            }
        }
    }

    fun loginWithRESTAPI(email: String, password: String, listener: (AuthListener) -> Unit){
        val loginLoader = LoginLoader()
        Log.d(TAG, "API Function Called!")

        loginLoader.getLoginCompleted(object: LoginListener{
            override fun getLoginDone(response: LoginResponse?, status: Boolean, message: String) {
                Log.d(TAG, "Inside get Login Lmao!")
                if(status){
                    if (response != null) {
//                        Log.d(TAG, "Passed Lmao!")
                        Constant.getInstance()?.setUserID(response.localId)
                        Log.d(TAG, "Local ID = ${response.localId}")
                        listener(AuthListener(true, "Login Successful with rest api "))
                    }else{
                        listener(AuthListener(false, "Login Failed "))
                        Log.d(TAG, "Failed Lmao!")
                    }
                }
            }
        }, email, password)
    }
}