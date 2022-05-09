package com.example.fundoonotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogout : Button
    private lateinit var toolbar : Toolbar
    private lateinit var drawer : DrawerLayout
    private var backPressedTime : Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        auth = Firebase.auth
        drawer = findViewById(R.id.drawer_layout)

        //Toolbar Updates
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else if(backPressedTime + 2000 > System.currentTimeMillis()){
            super .onBackPressed()
            return
        }
        else{
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val intentUserLogin = Intent(this, Authenticate::class.java )

        if(currentUser == null){
            startActivity(intentUserLogin)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile -> {
                //Open Profile Fragment
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.outerFlFragment, Profile())
                    addToBackStack(null)
                    commit()
                }
            }
            R.id.search_button -> {
                Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}