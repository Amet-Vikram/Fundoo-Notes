package com.example.fundoonotes.view.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.R
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"

//add this for nav drawer --> , NavigationView.OnNavigationItemSelectedListener
class MainActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private lateinit var toolbar : Toolbar
    private var backPressedTime : Long = 0
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        sharedViewModel = ViewModelProvider(this, SharedViewModelFactory(UserAuthService()))[SharedViewModel::class.java]


        //Fragment Injection
        observeAppNav()

    }

    private fun observeAppNav() {
        val currentUser = auth.currentUser
        if(currentUser == null){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, Login())
//                addToBackStack(null)
                commit()
            }
        }else{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, NoteFragment())
//                addToBackStack(null)
                commit()
            }
        }
    }


    override fun onResume() {
        super.onResume()

        observeAppNav()

    }

    override fun onBackPressed(){
        when {
//            supportFragmentManager.backStackEntryCount > 0 -> {
//                supportFragmentManager.popBackStackImmediate()
//            }
            backPressedTime + 2000 > System.currentTimeMillis() -> {
                super.onBackPressed()
                return
            }
            else -> {
                Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show()
            }
        }
        backPressedTime = System.currentTimeMillis()
    }

}