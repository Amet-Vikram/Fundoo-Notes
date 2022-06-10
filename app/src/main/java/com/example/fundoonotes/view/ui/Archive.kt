package com.example.fundoonotes.view.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.view.adapter.NoteAdapter
import com.example.fundoonotes.viewmodel.NoteViewModel
import com.example.fundoonotes.viewmodel.NoteViewModelFactory


class Archive : Fragment(R.layout.fragment_archive) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var btnBack: ImageView
    private var fetchedNotes =  ArrayList<Note>()
    private lateinit var noteVM: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteVM = ViewModelProvider(requireActivity(), NoteViewModelFactory(requireActivity()))[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_archive, container, false)
        btnBack = view.findViewById(R.id.btnBackArchive)
        recyclerView = view.findViewById(R.id.rcvArchivedNotes)
        getArchivedNote()
        return view
    }

    private fun getArchivedNote(){
        noteVM.fetchNotes()
        noteVM.noteList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { noteList ->
            if(noteList.isNotEmpty()){
                fetchedNotes.clear()
                for(note in noteList){
                    if(note.archive == 1){
                        fetchedNotes.add(note)
                    }
                }
            }
//            Log.d(TAG, " User Note List length in getNotes() = ${fetchedNotes.size}")
            noteAdapter = NoteAdapter(fetchedNotes, this.context)
            recyclerView.adapter = noteAdapter
        })
    }

}