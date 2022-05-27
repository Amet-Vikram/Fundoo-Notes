package com.example.fundoonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteListener
import com.example.fundoonotes.model.NoteService

class NoteViewModel(): ViewModel() {

    private val noteService = NoteService()

    private val _noteStatus = MutableLiveData<NoteListener>()
    val noteStatus: LiveData<NoteListener> = _noteStatus

    fun editNote(editedNote: Note) {

        noteService.updateNote(editedNote){
            _noteStatus.value = it
        }
    }

    fun deleteNote(noteID: String){

        noteService.deleteNote(noteID){
            _noteStatus.value = it
        }
    }

}