package com.example.fundoonotes.view.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.NoteViewModel
import com.example.fundoonotes.viewmodel.NoteViewModelFactory
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class AddEditNote() : Fragment() {


    private var noteID: String? = null
    private var title: String? = null
    private var desc: String? = null
    private var isUpdating: Boolean = false

    private lateinit var btnBack: ImageView
    private lateinit var btnPRed: ImageView
    private lateinit var btnPGreen: ImageView
    private lateinit var btnPBlue: ImageView
    private lateinit var btnPYellow: ImageView
    private lateinit var etNoteTitle: EditText
    private lateinit var etNoteDesc: EditText
    private lateinit var btnSaveNote: FloatingActionButton
    private lateinit var btnDelNote: FloatingActionButton
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var noteVM: NoteViewModel
    private var priority: Int = 4


    constructor(id: String, title: String, desc: String) : this() {
        isUpdating = true // Temporary
        this.noteID = id
        this.title = title
        this.desc = desc
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        noteVM = ViewModelProvider(requireActivity(), NoteViewModelFactory(requireActivity()))[NoteViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService()))[SharedViewModel::class.java]
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = requireView().findViewById(R.id.btnAddNoteToHome)
        btnPYellow = requireView().findViewById(R.id.priority_yellow)
        btnPRed = requireView().findViewById(R.id.priority_red)
        btnPGreen = requireView().findViewById(R.id.priority_green)
        btnPBlue = requireView().findViewById(R.id.priority_blue)
        btnSaveNote = requireView().findViewById(R.id.btnSaveNote)
        btnDelNote = requireView().findViewById(R.id.btnDeleteNote)
        etNoteTitle = requireView().findViewById(R.id.etNoteTitle)
        etNoteDesc = requireView().findViewById(R.id.etNoteDesc)

        btnDelNote.visibility = View.GONE

        if(isUpdating){
            btnDelNote.visibility = View.VISIBLE
            etNoteTitle.setText(title, TextView.BufferType.EDITABLE)
            etNoteTitle.requestFocus()
            etNoteDesc.setText(desc, TextView.BufferType.EDITABLE)
        }
    }

    override fun onStart() {
        super.onStart()

        btnPRed.setOnClickListener{
            priority = 1
            btnPRed.setImageResource(R.drawable.ic_save_note)
            btnPYellow.setImageResource(0)
            btnPGreen.setImageResource(0)
            btnPBlue.setImageResource(0)
        }

        btnPYellow.setOnClickListener{
            priority = 2
            btnPYellow.setImageResource(R.drawable.ic_save_note)
            btnPRed.setImageResource(0)
            btnPGreen.setImageResource(0)
            btnPBlue.setImageResource(0)
        }

        btnPGreen.setOnClickListener{
            priority = 3
            btnPGreen.setImageResource(R.drawable.ic_save_note)
            btnPRed.setImageResource(0)
            btnPYellow.setImageResource(0)
            btnPBlue.setImageResource(0)
        }

        btnPBlue.setOnClickListener{
            priority = 4
            btnPBlue.setImageResource(R.drawable.ic_save_note)
            btnPRed.setImageResource(0)
            btnPYellow.setImageResource(0)
            btnPGreen.setImageResource(0)
        }

        btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        btnSaveNote.setOnClickListener{
            if(isUpdating){
                editNote()
            }else{
                createNote()
            }
        }

        btnDelNote.setOnClickListener{
            deleteNote()
        }
    }

    private fun createNote() {
        val newNoteID = "${UUID.randomUUID()}"
        val noteTitle = etNoteTitle.text.toString()
        val note = etNoteDesc.text.toString()

        val currentDate = Date()
        val time: String = DateFormat.format("dd-MM-yyyy", currentDate.time).toString()

        val newNote = Note(
            newNoteID, noteTitle, note, priority, created = time
        )

        if(TextUtils.isEmpty(note)){
            etNoteDesc.error = "Note can't be empty"
            etNoteDesc.requestFocus()
        }else{
            noteVM.createNote(newNote)
            uiResponse()
        }
    }

    private fun editNote() {
        val editedNoteID = noteID!!
        val editedNoteTitle = etNoteTitle.text.toString()
        val editedNoteDesc = etNoteDesc.text.toString()
        val currentDate = Date()
        val time: String = DateFormat.format("dd-MM-yyyy", currentDate.time).toString()

        val editedNote = Note(
            editedNoteID, editedNoteTitle, editedNoteDesc, priority, created = time
        )
        if(TextUtils.isEmpty(editedNoteDesc)){
            etNoteDesc.error = "Note can't be empty"
            etNoteDesc.requestFocus()
        }else{
            noteVM.editNote(editedNote)
            uiResponse()
        }
    }

    private fun deleteNote() {
        noteVM.deleteNote(noteID!!)
        uiResponse()
    }

    private fun uiResponse(){
        noteVM.noteStatus.observe(viewLifecycleOwner, Observer {
            if(it.status){
                Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }else{
                Toast.makeText(this.context, it.message , Toast.LENGTH_SHORT).show()
            }
        })
    }
}