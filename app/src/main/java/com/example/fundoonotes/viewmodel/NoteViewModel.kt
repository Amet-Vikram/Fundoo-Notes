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

    init {
        noteDatabaseManager.open()
    }

    private var networkStatus = true

    private val _noteStatus = MutableLiveData<NoteListener>()
    val noteStatus: LiveData<NoteListener> = _noteStatus

    private val _getNoteList = MutableLiveData<ArrayList<Note>>()
    val getNoteList: LiveData<ArrayList<Note>> = _getNoteList

    fun createNote(newNote: Note){
        if(networkStatus){
            noteService.createNoteOnFireStore(newNote){
                _noteStatus.value = it
            }
            //Add note to sqlite as well
            noteDatabaseManager.createOfflineNote(newNote){
                _noteStatus.value = it
            }
        }else{
            //Add note to sqlite only
        }
    }

    fun editNote(editedNote: Note) {
        if(networkStatus){
            noteService.updateNoteOnFireStore(editedNote){
                _noteStatus.value = it
            }
            noteDatabaseManager.updateOfflineNote(editedNote){
                _noteStatus.value = it
            }
        }else{
            //Update note in sqlite only
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
                _getNoteList.value = it
            }
        }else{
            noteDatabaseManager.fetchAllOfflineNotes {
                _getNoteList.value = it
            }
        }
    }
}