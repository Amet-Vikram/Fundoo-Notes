package com.example.fundoonotes.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlin.collections.ArrayList


private const val TAG = "NoteFirebaseManager"
class NoteFirebaseManager() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userId : String
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var lastResult: DocumentSnapshot? = null
    private val userNotes = ArrayList<Note>()

    fun createNoteOnFireStore(
        newNote: Note,
        listener: (NoteListener) -> Unit
    ){
        userId = auth.currentUser?.uid.toString()
        val noteID = newNote.id
        val docReference: DocumentReference = db.collection("users").document(userId)
            .collection("userNotes").document(noteID)

        docReference.set(newNote).addOnCompleteListener {
            if(it.isSuccessful){
                //save note into sqlite db as well
                Log.d(TAG, "User note created for: $noteID")
                listener(NoteListener(true, "User note created!"))
            }else{
                listener(NoteListener(false, "Couldn't create note"))
            }
        }
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

    fun updateNoteOnFireStore(editedNote: Note, listener: (NoteListener) -> Unit) {
        userId = auth.currentUser?.uid.toString()
        val noteID = editedNote.id
        val docReference: DocumentReference = db.collection("users").document(userId)
            .collection("userNotes").document(noteID)

        docReference.get().addOnSuccessListener {
            if(it.exists()){
                Log.d(TAG,"Note Updated!")
                docReference.set(editedNote)
                listener(NoteListener(true, "Note updated!"))
            }else{
                Log.e(TAG,"Failed to update. Note Id: $noteID not found")
                listener(NoteListener(false, "Couldn't update note on cloud."))
            }
        }
    }

    fun deleteNoteFromFireStore(noteID: String, listener: (NoteListener) -> Unit){
        userId = auth.currentUser?.uid.toString()
        val userNoteReference: DocumentReference = db.collection("users").document(userId)
            .collection("userNotes").document(noteID)

        userNoteReference.delete().addOnCompleteListener {
            if(it.isSuccessful){
                listener(NoteListener(true, "Note Deleted!"))
            }else{
                Log.e(TAG,"Couldn't fetch noteID")
            }
        }
    }

    fun getNoteFromFireStore(listener: (ArrayList<Note>) -> Unit){
        val fetchedNotes = ArrayList<Note>()
        var note: Note
        val query: Query

        userId = auth.currentUser?.uid.toString()
        val allUserNoteReference = db.collection("users").document(userId)
            .collection("userNotes")

        if(lastResult == null){
            query = allUserNoteReference.orderBy("title").limit(10)
        }else{
            query = allUserNoteReference.orderBy("title")
                .startAfter(lastResult!!.data?.get("title"))
                .limit(10)
        }

        auth.currentUser.let {
            query.get()
                .addOnCompleteListener {
                if(it.isSuccessful && it.result != null){
                    for(documents in it.result!!){
                        note = documents.toObject<Note>()
                        fetchedNotes.add(note)
                    }
                    Log.d(TAG, "${fetchedNotes.size} notes fetched from Firebase")
                }else{
                    Log.d(TAG, "No notes Found")
                }
                userNotes.addAll(fetchedNotes)
                Log.d(TAG, "${userNotes.size} notes in User Notes")
                if(!it.result.isEmpty){
                    lastResult = it.result.documents[it.result.size() - 1]
                }
                listener(userNotes)
            }
        }
    }
}

data class Note(
    val id: String,
    val title: String = "title",
    val desc: String,
    val priority: Int = 4,
    val archive: Int,
    val created : String
){
    //to deserialize using Fire Store requires no-arg constructor.
    constructor() : this("", "", "", -1,-1,"")
}

data class NoteListener(val status: Boolean, val message: String)