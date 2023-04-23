package com.example.emosense

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okio.IOException


class NewEntryActivity : AppCompatActivity() {
    private lateinit var etEntry: EditText
//    private lateinit var tvDate: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)
        initWidgets()
//        tvDate!!.text = "Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate!!)
    }

    private fun initWidgets() {
        etEntry = findViewById(R.id.etEntry)
//        tvDate = findViewById(R.id.tvDate)
    }

    fun saveEventAction(view: View?) {
        //do network call to input APIFDDD
//        val eventName = etEntry!!.text.toString()
//        val newEvent = Event(eventName, CalendarUtils.selectedDate!!, time!!)
//        Event.eventsList.add(newEvent)
        val entry = etEntry.text.toString()
        if(entry.isNotEmpty()){
            val requestBody = FormBody.Builder()
                .add("username", "test")
                .add("entry", entry)
                .build()

            val headers = Headers.Builder()
//            .add("Authorization", "Bearer <your_token>")
                .add("Content-Type", "application/json")
                .add("ngrok-skip-browser-warning", "abc")
                .build()

            val request = Request.Builder()
                .url("${HttpClient.baseurl}/api/accounts/input/")
                .post(requestBody)
                .headers(headers)
                .build()

            HttpClient.client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    //response.body can only be consumed once from the buffer, so we have to save it (in OkHttp)
                    Log.d("TAG", "Input Successful: $entry")
                }


                override fun onFailure(call: Call, e: IOException) {
                    Log.d("TAG", "Input Network Error:$e")
                }
            })
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}