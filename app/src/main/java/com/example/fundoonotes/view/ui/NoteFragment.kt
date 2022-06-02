package com.example.fundoonotes.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.view.adapter.NoteAdapter
import com.example.fundoonotes.viewmodel.NoteViewModel
import com.example.fundoonotes.viewmodel.NoteViewModelFactory
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.collections.ArrayList

private const val TAG = "NoteFragment"
class NoteFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val profileDialog = ProfileDialogFragment()
    //UI elements
    private lateinit var drawer : DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnAddNote: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var menu: Menu
    private lateinit var ivProfile: CircleImageView
    private lateinit var searchButton: SearchView
    //ViewModels
    private lateinit var noteVM: NoteViewModel
    private lateinit var sharedVM: SharedViewModel
    //FireBase
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userId : String
    //RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private var userNotes =  ArrayList<Note>()
    private val LISTVIEW = "LIST_VIEW"
    private val GRIDVIEW = "GRID_VIEW"
    private lateinit var currentView: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteVM = ViewModelProvider(requireActivity(), NoteViewModelFactory(this.requireContext()))[NoteViewModel::class.java]
        sharedVM = ViewModelProvider(this, SharedViewModelFactory(UserAuthService()))[SharedViewModel::class.java]
        //Toolbar Options  toggle feature
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //Set toolbar
        val view: View = inflater.inflate(R.layout.fragment_notes, container, false)

        btnAddNote = view.findViewById(R.id.btnAddNote)
        toolbar = view.findViewById(R.id.toolbar)
        drawer = view.findViewById(R.id.drawer_layout)
        navigationView = view.findViewById(R.id.nav_view)
        recyclerView = view.findViewById(R.id.rcvAllNotes)
        ivProfile = view.findViewById(R.id.profile_image)
        searchButton = view.findViewById(R.id.search_button2)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        //To Hide the Title
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        getNotes()
        return view
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
                replace(R.id.flFragment, AddEditNote())
                addToBackStack(null)
                commit()
            }
        }

        //Recycler View Attributes
        listView()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu_main, menu)

        //updating the user profile image
        userId = auth.currentUser?.uid.toString()
        val storageRef = FirebaseStorage.getInstance().reference
        val imageDirRef = storageRef.child("users/$userId/profile.jpg")
        imageDirRef.downloadUrl.addOnSuccessListener { uri ->
            if(uri!= null){
                Picasso.get().load(uri).into(ivProfile)
            }
        }

        ivProfile.setOnClickListener {
            profileDialog.show(requireActivity().supportFragmentManager, "profileDialog")
        }

        searchButton.queryHint = "Search"
        searchButton.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                sharedVM.setQueryText(newText!!)
                sharedVM.queryText.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    noteAdapter.filter.filter(it)
                })
                return false
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val toggleButton = menu.getItem(0)

        when(item.itemId){
            R.id.toggleView -> {
                if(currentView == LISTVIEW){
                    toggleButton.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_list)
                    gridView()
                }else{
                    toggleButton.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid)
                    listView()
                }
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

    private fun getNotes(){
        noteVM.fetchNotes()
        noteVM.getNoteStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {noteList ->
            if(noteList.isNotEmpty()){
                userNotes.clear()

                for(note in noteList){
                    userNotes.add(note)
                }
                Log.d(TAG, " List length in getNotes() = ${userNotes.size}")
                noteAdapter = NoteAdapter(userNotes, this.context)
                recyclerView.adapter = noteAdapter
            }
        })
    }

    private fun listView() {
        currentView = LISTVIEW
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)
    }

    private fun gridView(){
        currentView = GRIDVIEW
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerView.setHasFixedSize(true)
    }
}