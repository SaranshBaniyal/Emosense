package com.example.emosense

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class CalendarAdapter(
    private val days: ArrayList<LocalDate>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        if (days.size > 15) //month view
            layoutParams.height = (parent.height * 0.166666666).toInt() else  // week view
            layoutParams.height = parent.height
        return CalendarViewHolder(view, onItemListener, days)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = days[position]
        holder.dayOfMonth.text = date!!.dayOfMonth.toString()
        if (date == CalendarUtils.selectedDate) {
            holder.parentView.setBackgroundColor(Color.parseColor("#17cf97"))
        }
        if (date.month == CalendarUtils.selectedDate?.getMonth()) holder.dayOfMonth.setTextColor(
            Color.WHITE
        )
        else holder.dayOfMonth.setTextColor(
            Color.LTGRAY
        )
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate)
    }
}
