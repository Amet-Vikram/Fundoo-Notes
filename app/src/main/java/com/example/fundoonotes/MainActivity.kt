package com.example.fundoonotes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogout : Button
    private lateinit var toolbar : Toolbar
    private lateinit var drawer : DrawerLayout
    private var backPressedTime : Long = 0
    private lateinit var navigationView: NavigationView
    private val profileDialog = ProfileDialogFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        auth = Firebase.auth
        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        //Show Note Fragment as default
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, Notes())
            commit()
        }

        //Toolbar Updates
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //Fragment Navigation
        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.notes -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, Notes())
                    addToBackStack(null)
                    commit()
                }
            }
            R.id.reminder -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, Reminder())
                    addToBackStack(null)
                    commit()
                }
            }
            R.id.archive -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, Archive())
                    addToBackStack(null)
                    commit()
                }
            }
            R.id.trash -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, Trash())
                    addToBackStack(null)
                    commit()
                }
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed(){
//        if (drawer.isDrawerOpen(GravityCompat.START)){
//            drawer.closeDrawer(GravityCompat.START)
//        }else
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
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
                profileDialog.show(supportFragmentManager, "profileDialog")
            }
            R.id.search_button -> {
                Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}