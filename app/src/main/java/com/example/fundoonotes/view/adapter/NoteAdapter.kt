package com.example.fundoonotes.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note

class NoteAdapter(private var noteList: ArrayList<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)

        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val currentItem = noteList[position]
        holder.noteTitle.text = currentItem.title
        holder.noteBody.text = currentItem.desc
        holder.noteCreated.text = currentItem.created
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notePriority: ImageView = itemView.findViewById(R.id.item_priority)
        val noteTitle: TextView = itemView.findViewById(R.id.item_title)
        val noteBody: TextView = itemView.findViewById(R.id.item_desc)
        val noteCreated: TextView = itemView.findViewById(R.id.item_date)
    }
}


