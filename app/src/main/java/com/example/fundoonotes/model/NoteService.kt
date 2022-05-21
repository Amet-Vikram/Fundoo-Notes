package com.example.fundoonotes.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


private const val TAG = "NoteService"
class NoteService() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userId : String
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun createNote(
        newNote: Note,
        listener: (NoteListener) -> Unit,
    ){
        userId = auth.currentUser?.uid.toString()
        val noteID = newNote.id
        val docReference: DocumentReference = db.collection("users").document(userId)
            .collection("userNotes").document(noteID)

        docReference.set(newNote).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d(TAG, "User note created for: $noteID")
                listener(NoteListener(true, "User note created!"))
            }else{
                listener(NoteListener(false, "Couldn't create note"))
            }
        }
    }

    fun readNote(){}

    fun updateNote(){}

    fun deleteNote(){}
}

data class Note(
    val id: String,
    val title: String = "title",
    val desc: String,
    val priority: Int = 3,
    val isArchive: Boolean = false,
    val created : String
)

data class NoteListener(val status: Boolean, val message: String)