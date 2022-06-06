package com.example.fundoonotes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteListener
import com.example.fundoonotes.model.NoteFirebaseManager
import com.example.fundoonotes.model.dao.NoteDatabaseManager

private const val TAG = "NoteViewModel"
class NoteViewModel(context: Context): ViewModel() {

    private val noteService = NoteFirebaseManager()
    private val noteDatabaseManager = NoteDatabaseManager(context)

    init {
        noteDatabaseManager.open()
    }

    private var networkStatus = isConnectedToInternet()

    private val _noteStatus = MutableLiveData<NoteListener>()
    val noteStatus: LiveData<NoteListener> = _noteStatus

    private val _noteList = MutableLiveData<ArrayList<Note>>()
    val noteList: LiveData<ArrayList<Note>> = _noteList

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
            noteDatabaseManager.createOfflineNote(newNote){
                _noteStatus.value = it
            }
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
            noteDatabaseManager.updateOfflineNote(editedNote){
                _noteStatus.value = it
            }
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
            noteDatabaseManager.deleteOfflineNote(noteID){
                _noteStatus.value = it
            }
        }
    }

    fun fetchNotes(){
        if(networkStatus){
            noteService.getNoteFromFireStore {
                _noteList.value = it
            }
        }else{
            noteDatabaseManager.fetchAllOfflineNotes {
                _noteList.value = it
            }
        }
    }

    private fun isConnectedToInternet(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

    fun closeDB(){
        noteDatabaseManager.close()
    }
}