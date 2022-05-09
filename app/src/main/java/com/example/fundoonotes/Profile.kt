package com.example.fundoonotes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Profile : Fragment(R.layout.fragment_profile) {

    private lateinit var ivBackButton : ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var tvLogout : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivBackButton = requireView().findViewById(R.id.btnBackProfile)
        tvLogout = requireView().findViewById(R.id.tvLogout)
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        val intentUserLogin = Intent(this.context, Authenticate::class.java )

        ivBackButton.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        tvLogout.setOnClickListener{
            auth.signOut()
            startActivity(intentUserLogin)
        }
    }
}