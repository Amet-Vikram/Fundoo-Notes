package com.example.fundoonotes.view.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.R
import com.example.fundoonotes.model.User
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

private const val TAG = "ProfileDialogFragment"

class ProfileDialogFragment: DialogFragment(R.layout.fragment_dialog_profile) {
    //UI
    private lateinit var btnBack : ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var tvLogout : TextView
    private lateinit var tvFName : TextView
    private lateinit var tvEmail : TextView
    private lateinit var profilePic: ImageView

    //Fire Base
    private lateinit var db : FirebaseFirestore
    private lateinit var userID: String
    private lateinit var storageRef : StorageReference
    private lateinit var imageURI : Uri

    //Result launcher for fetching profile picture
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userID = auth.currentUser?.uid.toString()
        storageRef = FirebaseStorage.getInstance().reference

        resultLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageURI= result.data?.data!!

                uploadImageToFirebase(imageURI)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = requireView().findViewById(R.id.btnBackProfile1)
        tvLogout = requireView().findViewById(R.id.tvLogout)
        tvEmail = requireView().findViewById(R.id.tvEmail)
        tvFName = requireView().findViewById(R.id.tvFName)
        profilePic = requireView().findViewById(R.id.profilePicture)


        loadUserData()
    }

    override fun onStart() {
        super.onStart()

        val intentUserLogin = Intent(this.context, MainActivity::class.java )

        //Exit dialog fragment
        btnBack.setOnClickListener{
            dismiss()
        }

        //Logout from current profile
        tvLogout.setOnClickListener{
            auth.signOut()
            startActivity(intentUserLogin)
        }

        //Upload/update profile picture
        profilePic.setOnClickListener{
            val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intentGallery)
        }

    }

    //UI UPDATE WRITE ONLY
    private fun uploadImageToFirebase(imageURI: Uri) {
        //Need Storage Reference, Image Uri and filename
        val imageDirRef = storageRef.child("users/$userID/profile.jpg")

        imageDirRef.putFile(imageURI).addOnSuccessListener {
            if(it.error == null){
                Toast.makeText(this.context, "Image Uploaded!", Toast.LENGTH_SHORT).show()
                imageDirRef.downloadUrl.addOnSuccessListener { uri ->
                    Picasso.get().load(uri).into(profilePic)
                }
            }
            else{
                Toast.makeText(this.context, "Upload Failed!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Upload failed: ${it.error}")
            }
        }
    }


    //Load data from user Collection
    private fun loadUserData() {

        val userDetails = db.collection("users").document(userID)
        var currentUser : User?
        var profileRef: StorageReference

        userDetails.get().addOnSuccessListener {
            if(it.exists()){
                Log.i(TAG, "Fetched User Details")
                val fullName: String = it.getString("fName") + " " + it.getString("lName")
                val email: String = it.getString("eMail").toString()

                currentUser = User(userID,fullName, email)

                Log.i(TAG, "Got Current User!!")
                tvFName.text = currentUser!!.fullName
                tvEmail.text = currentUser!!.eMail

                //Fetching User Image
                profileRef = storageRef.child("users/$userID/profile.jpg")

                profileRef.downloadUrl.addOnSuccessListener { uri ->
                    Picasso.get().load(uri).into(profilePic)
                }
            }
            else{
                Log.e(TAG, "Failed to get User Details")
            }
        }
    }
}