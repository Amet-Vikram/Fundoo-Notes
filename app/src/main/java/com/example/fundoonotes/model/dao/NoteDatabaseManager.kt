package com.example.fundoonotes.model.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.fundoonotes.model.DatabaseHelper
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteListener

private const val TAG = "NoteDatabaseManager"
class NoteDatabaseManager(private var context: Context){
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var database: SQLiteDatabase

    fun open(): NoteDatabaseManager {
        dbHelper = DatabaseHelper(context)
        database = dbHelper.readableDatabase
        return this
    }

    fun close(){
        if(database.isOpen){
            database.close()
        }
        dbHelper.close()
    }

    fun createOfflineNote(note: Note, listener: (NoteListener) -> Unit){
        val cv = ContentValues()
        database = dbHelper.writableDatabase

        cv.put(dbHelper.COLUMN_ID, note.id)
        cv.put(dbHelper.COLUMN_TITLE, note.title)
        cv.put(dbHelper.COLUMN_DESC, note.desc)
        cv.put(dbHelper.COLUMN_PRIORITY, note.priority)
        cv.put(dbHelper.COLUMN_ARCHIVE, note.archive)
        cv.put(dbHelper.COLUMN_CREATED, note.created)

        val insert = database.insert(dbHelper.TABLE_NAME, null, cv)

        if (insert >= 0){
            Log.d(TAG, "Note Stored in SQLITE: ${dbHelper.COLUMN_TITLE}")
            listener(NoteListener(true, "User note created!"))
        }else{
            Log.d(TAG, "Couldn't Store: $insert")
            listener(NoteListener(false, "Couldn't create note"))
        }
    }

    fun fetchAllOfflineNotes(listener: (ArrayList<Note>) -> Unit){
        val result = ArrayList<Note>()
        //fetching notes
        val queryFetch = "SELECT * FROM ${dbHelper.TABLE_NAME}"
        database = dbHelper.readableDatabase

        val cursor: Cursor = database.rawQuery(queryFetch, null)

        if(cursor.moveToFirst()){
            do {
                val id = cursor.getString(0)
                val title = cursor.getString(1)
                val desc = cursor.getString(2)
                val priority = cursor.getInt(3)
                val archived = cursor.getInt(4)
                val created = cursor.getString(5)

                val noteEntry = Note(id, title, desc, priority, archived, created)
                result.add(noteEntry)
            }while (cursor.moveToNext())
        }else {
            //Don't add anything
        }
        listener(result)
        cursor.close()
    }

    fun updateOfflineNote(editedNote: Note, listener: (NoteListener) -> Unit){
        val cv = ContentValues()
        database = dbHelper.writableDatabase

        cv.put(dbHelper.COLUMN_ID, editedNote.id)
        cv.put(dbHelper.COLUMN_TITLE, editedNote.title)
        cv.put(dbHelper.COLUMN_DESC, editedNote.desc)
        cv.put(dbHelper.COLUMN_PRIORITY, editedNote.priority)
        cv.put(dbHelper.COLUMN_ARCHIVE, editedNote.archive)
        cv.put(dbHelper.COLUMN_CREATED, editedNote.created)

        val i = database.update(dbHelper.TABLE_NAME, cv, "${dbHelper.COLUMN_ID} = ?", arrayOf(editedNote.id))

        if(i >= 1){
            listener(NoteListener(true, "Note updated!"))
        }else{
            listener(NoteListener(false, "Couldn't update offline note."))
        }
    }

    fun deleteOfflineNote(noteID: String, listener: (NoteListener) -> Unit){
        database = dbHelper.writableDatabase
        val queryDelete = "DELETE FROM ${dbHelper.TABLE_NAME} WHERE ${dbHelper.COLUMN_ID} = ?"

        Log.d(TAG, "Note Deleted in SQLITE: $noteID")

        val cursor: Cursor = database.rawQuery(queryDelete, arrayOf(noteID))

        if(cursor.moveToFirst()){
            listener(NoteListener(true, "Note Deleted!"))
        }

        cursor.close()
    }
}