package com.example.fundoonotes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

private const val TAG = "ProfileDialogFragment"

class ProfileDialogFragment: DialogFragment(R.layout.fragment_dialog_profile) {
    private lateinit var btnBack : ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var tvLogout : TextView
    private lateinit var tvFName : TextView
    private lateinit var tvEmail : TextView
    private lateinit var db : FirebaseFirestore
    private lateinit var userID: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = requireView().findViewById(R.id.btnBackProfile1)
        tvLogout = requireView().findViewById(R.id.tvLogout)
        tvEmail = requireView().findViewById(R.id.tvEmail)
        tvFName = requireView().findViewById(R.id.tvFName)
    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userID = auth.currentUser?.uid.toString()

        val intentUserLogin = Intent(this.context, Authenticate::class.java )

        btnBack.setOnClickListener{
            dismiss()
        }

        tvLogout.setOnClickListener{
            auth.signOut()
            startActivity(intentUserLogin)
        }

        loadData(userID)

    }

    //Load data from user Collection to Text Views
    private fun loadData(userID: String) {

        val userDetails = db.collection("users").document(userID)

        userDetails.get().addOnSuccessListener {
            if(it.exists()){
                val fullName: String = it.getString("fName") + " " + it.getString("lName")
                tvFName.text = fullName
                tvEmail.text = it.getString("eMail")
            }
            else{
                Log.e(TAG, "Failed to get User Details")
            }
        }
    }

}