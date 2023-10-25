package com.example.emosense

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emosense.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import okio.IOException
import java.time.LocalDate


class MainActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {
    lateinit var binding: ActivityMainBinding
    val gson = Gson()
    lateinit var adapter: JournalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setWeekView()

    }

    open fun setWeekView() {
        binding.monthYearTV.text = CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate)
        val days: ArrayList<LocalDate> = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate)
        val calendarAdapter = CalendarAdapter(days, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter
        setEventAdapter()
    }


    fun previousWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.plusWeeks(1)
        setWeekView()
    }

    open fun setEventAdapter() {
        var dailyJournal = listOf<Journal>()
        adapter = JournalAdapter(this, dailyJournal)
        binding.rvJournal.layoutManager = LinearLayoutManager(this)
        binding.rvJournal.adapter = adapter

        adapter.onItemClick = {
            val intent = Intent(this, MusicActivity::class.java)
            intent.putExtra("Journal",it)
            startActivity(intent)
        }


        val requestBody = FormBody.Builder()
            .add("username", HttpClient.username)
            .add("date", CalendarUtils.selectedDate.toString())
            .build()

        val headers = Headers.Builder()
//            .add("Authorization", "Bearer <your_token>")
            .add("Content-Type", "application/json")
            .add("ngrok-skip-browser-warning", "abc")
            .build()

        val request = Request.Builder()
            .url("${HttpClient.baseurl}/api/accounts/output/")
            .post(requestBody)
            .headers(headers)
            .build()

        HttpClient.client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                //response.body can only be consumed once from the buffer, so we have to save it (in OkHttp)
                val body: ResponseBody? = response.body
                if (body != null) {
                    val jsonString = body!!.string()
                    val journalArray = gson.fromJson(jsonString, Array<Journal>::class.java)
                    dailyJournal = journalArray.toList()

                    adapter.journalList = dailyJournal
                        runOnUiThread {
                            adapter.notifyDataSetChanged()
                        }

                    Log.d("TAG", "Output Request Data:$dailyJournal")
                } else {
                    Log.d("TAG", "Response body is null")
                }
            }


            override fun onFailure(call: Call, e: IOException) {
                Log.d("TAG", "Output Network Error:$e")
            }
        })


    }

    fun newEventAction(view: View?) {
        startActivity(Intent(this, NewEntryActivity::class.java))
    }

    override fun onItemClick(position: Int, date: LocalDate) {
        CalendarUtils.selectedDate = date
        setWeekView()
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG","OnResume()")
        setEventAdapter()
    }

    fun setReminderAction(view: View) {
        startActivity(Intent(this@MainActivity, ReminderActivity::class.java))
    }

}