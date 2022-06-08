package com.example.fundoonotes.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.*


private const val TAG = "SharedViewModel"

class SharedViewModel(private val userAuthService: UserAuthService): ViewModel() {

    private val noteService = NoteFirebaseManager()

    private val _userDetails = MutableLiveData<User>()
    val useDetails: LiveData<User> = _userDetails

    private val _noteStatus = MutableLiveData<NoteListener>()
    val noteStatus: LiveData<NoteListener> = _noteStatus

    private val _userNoteList = MutableLiveData<ArrayList<Note>>()
    val userNoteList: LiveData<ArrayList<Note>> = _userNoteList

    private val _queryText = MutableLiveData<String>()
    val queryText: LiveData<String> = _queryText

    fun loadUserData(){
         userAuthService.loadUserData(){
             Log.i(TAG, "Mutable Data Updated")
             _userDetails.value = it
         }
    }

    fun createNote(newNote: Note){
        noteService.createNoteOnFireStore(newNote){
            _noteStatus.value = it
        }
    }

    fun setQueryText(query: String){
        _queryText.value = query
    }
}