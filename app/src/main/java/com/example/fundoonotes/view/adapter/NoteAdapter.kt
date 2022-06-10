package com.example.fundoonotes.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.view.ui.AddEditNote
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "NoteAdapter"
class NoteAdapter(private var noteList: ArrayList<Note>, private val context: Context?) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(), Filterable {

    var filteredList = ArrayList<Note>()

    init {
        filteredList = this.noteList
    }

    private lateinit var activity: AppCompatActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)

        return NoteViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentItem = filteredList[position]

        Log.d(TAG, "${currentItem.title} is Archived ${currentItem.archive}")

        if(currentItem.archive == 1){
            Log.d(TAG, "Current Item is Archived")
            holder.itemView.isVisible = false
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0,0)
//            View.VISIBLE
        }else{
            holder.itemView.isVisible = true
//            Log.d(TAG, "Current Item is not Archived")
            holder.noteTitle.text = currentItem.title
            holder.noteBody.text = currentItem.desc
            holder.noteCreated.text = currentItem.created

            holder.itemView.setOnClickListener {
                activity = context as AppCompatActivity
                Toast.makeText(context, "Note title: ${currentItem.title}", Toast.LENGTH_SHORT).show()
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, AddEditNote(currentItem.id, currentItem.title, currentItem.desc))
                    .addToBackStack(null)
                    .commit()
            }

            when(currentItem.priority){
                1 -> holder.itemView.background = context?.let { ContextCompat.getDrawable(it, R.drawable.red_dot) }
                2 -> holder.itemView.background = context?.let { ContextCompat.getDrawable(it, R.drawable.yellow_dot) }
                3 -> holder.itemView.background = context?.let { ContextCompat.getDrawable(it, R.drawable.green_dot) }
                4 -> holder.itemView.background = context?.let { ContextCompat.getDrawable(it, R.drawable.blue_dot) }
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noteTitle: TextView = itemView.findViewById(R.id.item_title)
        val noteBody: TextView = itemView.findViewById(R.id.item_desc)
        val noteCreated: TextView = itemView.findViewById(R.id.item_date)
    }


    override fun getFilter(): Filter {
        return object: Filter(){
            //Background Thread
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                if(charSequence.toString().isEmpty()){
                    filteredList = noteList
                    Log.d(TAG, "Size if search text empty = ${filteredList.size}")
                }
                else{
                    val resultList = ArrayList<Note>()
                    for(note in noteList){
                        if(note.title.lowercase(Locale.getDefault()).contains(charSequence.toString()) || note.desc.lowercase(
                                Locale.getDefault()).contains(charSequence.toString())){
                            resultList.add(note)
                            Log.d(TAG, "Size result list = ${filteredList.size}")
                        }
                        filteredList = resultList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                Log.d(TAG, "Size of filtered list with result list = ${filteredList.size}")
                return filterResults
            }

            //UI Thread
            override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
                filteredList= filterResults?.values as ArrayList<Note>
                notifyDataSetChanged()
            }
        }
    }
}


