package com.example.fundoonotes.view.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.fundoonotes.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class NoteFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val profileDialog = ProfileDialogFragment()
    private lateinit var toolbar : Toolbar
    private lateinit var drawer : DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var btnAddNote: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Toolbar Options  toggle feature
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Set toolbar
        val view: View = inflater.inflate(R.layout.fragment_notes, container, false)
        val toolbar: Toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddNote = requireView().findViewById(R.id.btnAddNote)
        toolbar = requireView().findViewById(R.id.toolbar)
        drawer = requireView().findViewById(R.id.drawer_layout)
        navigationView = requireView().findViewById(R.id.nav_view)
    }

    override fun onStart() {
        super.onStart()

        val toggle = ActionBarDrawerToggle(requireActivity(), drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //Fragment Navigation
        navigationView.setNavigationItemSelectedListener(this)

        btnAddNote.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, AddNote())
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile -> {
                //Open Profile Fragment
                profileDialog.show(requireActivity().supportFragmentManager, "profileDialog")
            }
            R.id.search_button -> {
                Toast.makeText(this.context, "Searching...", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.notes -> {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, NoteFragment())
                    commit()
                }
            }
            R.id.reminder -> {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, Reminder())
                    commit()
                }
            }
            R.id.archive -> {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, Archive())
                    commit()
                }
            }
            R.id.trash -> {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, Trash())
                    commit()
                }
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}