package com.example.fundoonotes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteDatabaseManager
import com.example.fundoonotes.model.NoteListener
import com.example.fundoonotes.model.NoteFirebaseManager
private const val TAG = "NoteViewModel"
class NoteViewModel(context: Context): ViewModel() {

    private val noteService = NoteFirebaseManager()
    private val noteDatabaseManager = NoteDatabaseManager(context)

    private var networkStatus = true

    private val _noteStatus = MutableLiveData<NoteListener>()
    val noteStatus: LiveData<NoteListener> = _noteStatus

    private val _getNoteStatus = MutableLiveData<ArrayList<Note>>()
    val getNoteStatus: LiveData<ArrayList<Note>> = _getNoteStatus

    fun createNote(newNote: Note){
        if(networkStatus){
            noteService.createNoteOnFireStore(newNote){
                _noteStatus.value = it
            }
            //Add note to sqlite as well
            noteDatabaseManager.open()
            noteDatabaseManager.createOfflineNote(newNote){
                _noteStatus.value = it
            }
        }else{
            //Add note to sqlite only
        }
    }

    fun editNote(editedNote: Note) {
        noteService.updateNoteOnFireStore(editedNote){
            _noteStatus.value = it
        }
    }

    fun deleteNote(noteID: String){
        if(networkStatus){
            noteService.deleteNoteFromFireStore(noteID){
                _noteStatus.value = it
            }
            noteDatabaseManager.deleteOfflineNote(noteID){
                _noteStatus.value = it
            }
        }else{
            //Delete note in sqlite only
        }
    }

    fun fetchNotes(){
        if(networkStatus){
            noteService.getNoteFromFireStore {
                _getNoteStatus.value = it
            }
        }
    }
}