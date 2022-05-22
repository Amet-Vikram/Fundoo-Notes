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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class AddNote : Fragment() {

    private lateinit var btnBack: ImageView
    private lateinit var btnPRed: ImageView
    private lateinit var btnPGreen: ImageView
    private lateinit var btnPYellow: ImageView
    private lateinit var etNoteTitle: EditText
    private lateinit var etNoteDesc: EditText
    private lateinit var btnSaveNote: FloatingActionButton
    private lateinit var sharedViewModel: SharedViewModel
    private var priority: Int = 3

    override fun onAttach(context: Context) {
        super.onAttach(context)

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
        btnSaveNote = requireView().findViewById(R.id.btnSaveNote)
        etNoteTitle = requireView().findViewById(R.id.etNoteTitle)
        etNoteDesc = requireView().findViewById(R.id.etNoteDesc)
    }

    override fun onStart() {
        super.onStart()

        btnPRed.setOnClickListener{
            priority = 1
            btnPRed.setImageResource(R.drawable.ic_save_note)
            btnPYellow.setImageResource(0)
            btnPGreen.setImageResource(0)
        }

        btnPYellow.setOnClickListener{
            priority = 2
            btnPYellow.setImageResource(R.drawable.ic_save_note)
            btnPRed.setImageResource(0)
            btnPGreen.setImageResource(0)
        }

        btnPGreen.setOnClickListener{
            priority = 3
            btnPGreen.setImageResource(R.drawable.ic_save_note)
            btnPRed.setImageResource(0)
            btnPYellow.setImageResource(0)
        }

        btnBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        btnSaveNote.setOnClickListener{
            createNote()
        }
    }

    private fun createNote() {
        val noteID = "${UUID.randomUUID()}"
        val noteTitle = etNoteTitle.text.toString()
        val note = etNoteDesc.text.toString()
//        val time: String = Calendar.getInstance().time.toString()
        val currentDate = Date()
        val time: String = DateFormat.format("dd-MM-yyyy", currentDate.time).toString()

        val newNote = Note(
            noteID, noteTitle, note, priority, created = time
        )

        if(TextUtils.isEmpty(note)){
            etNoteDesc.error = "Note can't be empty"
            etNoteDesc.requestFocus()
        }else{
            sharedViewModel.createNote(newNote)

            sharedViewModel.noteStatus.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }else{
                    Toast.makeText(this.context, it.message , Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}