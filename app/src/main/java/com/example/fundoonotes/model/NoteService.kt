package com.example.fundoonotes.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "NoteService"
class NoteService() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userId : String
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    val gson = Gson()

    fun createNote(
        newNote: Note,
        listener: (NoteListener) -> Unit,
    ){
        userId = auth.currentUser?.uid.toString()
        val noteID = newNote.id
        val docReference: DocumentReference = db.collection("users").document(userId)
            .collection("userNotes").document(noteID)

//        val newNote1 = hashMapOf(
//            "id" to newNote.id,
//            "title" to newNote.title,
//            "desc" to newNote.desc,
//            "priority" to newNote.priority,
//            "archived" to newNote.isArchive,
//            "created" to newNote.created
//        )

        docReference.set(newNote).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d(TAG, "User note created for: $noteID")
                listener(NoteListener(true, "User note created!"))
            }else{
                listener(NoteListener(false, "Couldn't create note"))
            }
        }

//        docReference.set(newNote1).addOnCompleteListener {
//            if(it.isSuccessful){
//                Log.d(TAG, "User note created for: $noteID")
//                listener(NoteListener(true, "User note created!"))
//            }else{
//                listener(NoteListener(false, "Couldn't create note"))
//            }
//        }
    }

    fun readNote(isClosed: Boolean, noteList: (ArrayList<Note>) -> Unit){
        userId = auth.currentUser?.uid.toString()
        val docReference = db.collection("users").document(userId)
            .collection("userNotes")

        val noteUpdates = docReference.addSnapshotListener { result, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            val userNotes = ArrayList<Note>()

            if (result != null && !result.isEmpty) {
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
//                    Log.d(TAG, "Json String => $jsonString")

                    val userNote = document.toObject<Note>()
                    userNotes.add(userNote)
//                    Log.d(TAG, "Json String => $userNotes")
                }
                noteList(userNotes)
//                listener(NoteListener(true, "User note fetched"))
            } else {
                Log.d(TAG, "Current data: null")
//                Log.d(TAG, "Error getting documents: ")
//                listener(NoteListener(false, "Current data: null"))
            }
        }

        if (isClosed) {
            Log.d(TAG, "Listener Closed")
            noteUpdates.remove()
        }
    }

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
){
    //to deserialize using Fire Store requires no-arg constructor.
    constructor() : this("", "", "", -1, false, "")
}

data class NoteListener(val status: Boolean, val message: String)