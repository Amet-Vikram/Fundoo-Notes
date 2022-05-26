package com.example.fundoonotes.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.view.ui.EditNoteFragment

class NoteAdapter(private var noteList: ArrayList<Note>, private val context: Context?) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private lateinit var activity: AppCompatActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)

        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val currentItem = noteList[position]

        holder.noteTitle.text = currentItem.title
        holder.noteBody.text = currentItem.desc
        holder.noteCreated.text = currentItem.created


        holder.itemView.setOnClickListener {
            activity = context as AppCompatActivity
            Toast.makeText(context, "Note title: ${currentItem.title}", Toast.LENGTH_SHORT).show()
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, EditNoteFragment(currentItem.id, currentItem.title, currentItem.desc))
                .addToBackStack(null)
                .commit()
        }

        when(currentItem.priority){
            1 -> holder.itemView.background = context?.let { ContextCompat.getDrawable(it, R.drawable.red_dot) }
            2 -> holder.itemView.background = context?.let { ContextCompat.getDrawable(it, R.drawable.yellow_dot) }
            3 -> holder.itemView.background = context?.let { ContextCompat.getDrawable(it, R.drawable.green_dot) }
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


