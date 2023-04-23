package com.example.emosense

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class JournalAdapter(val context: Context, var journalList: List<Journal>) :
    RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    //custom function, for onclick reference CodingStuff video
    var onItemClick : ((Journal) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.journal_view, parent, false)
        return JournalViewHolder(view)
    }

    override fun getItemCount(): Int {
        //The ?: operator means "if journalList is not null, return its size, otherwise return 0".

        if (journalList.isEmpty())
            return 0

        return journalList.size
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        //since we have modified getItemCount to return 0 when journalList is null
        //this function will only get called when journalList is not null
        //hence !! assertion is used
        holder.tvEntry.text = journalList[position].entry
        holder.tvLabel.text = journalList[position].label

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(journalList[position])
        }
    }

    class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvEntry = itemView.findViewById<TextView>(R.id.tvEntry)
        var tvLabel = itemView.findViewById<TextView>(R.id.tvLabel)
    }
}
