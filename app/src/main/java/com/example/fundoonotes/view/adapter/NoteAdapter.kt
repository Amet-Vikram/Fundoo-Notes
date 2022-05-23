package com.example.fundoonotes.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note

class NoteAdapter(private var noteList: ArrayList<Note>, private val context: Context?) :
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

        when(currentItem.priority){
            1 -> holder.noteTitle.background = context?.let { ContextCompat.getDrawable(it, R.drawable.red_dot) }
            2 -> holder.noteTitle.background = context?.let { ContextCompat.getDrawable(it, R.drawable.yellow_dot) }
            3 -> holder.noteTitle.background = context?.let { ContextCompat.getDrawable(it, R.drawable.green_dot) }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val notePriority: ImageView = itemView.findViewById(R.id.item_priority)
        val noteTitle: TextView = itemView.findViewById(R.id.item_title)
        val noteBody: TextView = itemView.findViewById(R.id.item_desc)
        val noteCreated: TextView = itemView.findViewById(R.id.item_date)
    }
}


