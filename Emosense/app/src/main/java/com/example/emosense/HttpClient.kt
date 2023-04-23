package com.example.emosense
import okhttp3.OkHttpClient

object HttpClient {
    val baseurl = "https://b1e1-220-158-168-162.ngrok-free.app"
    lateinit var username: String
    val client = OkHttpClient()
}