package com.example.emosense

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emosense.databinding.ActivityMusicBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.Request
import okio.IOException
import org.json.JSONObject

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    val apiKey = "Youtube_Data_API_Key" //removed for security reasons

    val emotionMap = mapOf(
        "admiration" to "Classical music | orchestral music | choral music pieces.",
        "amusement" to "Pop music | rock music with upbeat | catchy melodies.",
        "anger" to "Heavy metal music | punk rock with aggressive | intense music sounds and lyrics.",
        "annoyance" to "Experimental music | avant-garde music",
        "approval" to "Pop music | indie rock music",
        "caring" to "Folk music | acoustic music | soothing melodies",
        "confusion" to "Ambient music | experimental electronic music",
        "curiosity" to "World music | intriguing jazz music",
        "desire" to "R&B music | soul music | seductive music",
        "disappointment" to "Indie music | alternative rock music | melancholic melodies.",
        "disapproval" to "protest music | rage music",
        "disgust" to "peaceful music",
        "embarrassment" to "self love music",
        "excitement" to "EDM | techno music | energetic sounds.",
        "fear" to "Horror movie soundtracks | dark ambient music",
        "gratitude" to "spiritual music",
        "grief" to "Classical sad music| grief instrumental music",
        "joy" to "Pop music | celebration music",
        "love" to "romantic music | R&B music",
        "nervousness" to "ambient music | meditative music",
        "optimism" to "uplifting music | inspiring music",
        "pride" to "Hip-hop music | rap music | empowering music",
        "realization" to "Progressive rock music",
        "relief" to "relaxing music | ambient music soothing",
        "remorse" to "Blues music | country music with soulful lyrics",
        "sadness" to "melancholic music | Classical sad music",
        "surprise" to "Pop music | rock music",
        "neutral" to "Ambient music | calming music"
    )
    val gson = Gson()
    lateinit var adapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val journal = intent.getParcelableExtra<Journal>("Journal")

        if (journal != null) {
            binding.tvHead.text = journal.entry
            binding.tvHeadLabel.text = journal.label
            binding.tvGenre.text = emotionMap[journal.label]

            var videos = listOf<Video>()
            adapter = MusicAdapter(this, videos)
            binding.rvMusic.layoutManager = LinearLayoutManager(this)
            binding.rvMusic.adapter = adapter

            adapter.onItemClick = {
//                val intent = Intent(this, MusicActivity::class.java)
//                intent.putExtra("Journal",it)
//                startActivity(intent)

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:${it.videoId}"))
                intent.putExtra("VIDEO_ID", it.videoId)
                intent.putExtra("force_fullscreen", true)
                startActivity(intent)
            }



            lifecycleScope.launch(Dispatchers.IO) {
                videos = searchByGenre(emotionMap[journal.label]!!)
                adapter.videoList = videos
                runOnUiThread {
//                    binding.tvMusic.text = videos.toString()
                    adapter.notifyDataSetChanged()
                }
                Log.d("TAG",videos.toString())
            }
        }
    }
    fun searchByGenre(genre: String): List<Video> {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("www.googleapis.com")
            .addPathSegment("youtube")
            .addPathSegment("v3")
            .addPathSegment("search")
            .addQueryParameter("part", "snippet")
            .addQueryParameter("type", "video")
            .addQueryParameter("q", genre)
            .addQueryParameter("key", apiKey)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = HttpClient.client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }

        val responseBody = response.body?.string() ?: throw IOException("Empty response body")
        val jsonObject = JSONObject(responseBody)

        val videos = mutableListOf<Video>()
        val itemsArray = jsonObject.getJSONArray("items")

        for (i in 0 until itemsArray.length()) {
            val item = itemsArray.getJSONObject(i)
            val videoId = item.getJSONObject("id").getString("videoId")
            val title = item.getJSONObject("snippet").getString("title")
            val thumbnailUrl = item.getJSONObject("snippet")
                .getJSONObject("thumbnails")
                .getJSONObject("default")
                .getString("url")
            videos.add(Video(videoId, title, thumbnailUrl))
        }

        return videos
    }
}
data class Video(val videoId: String, val title: String, val thumbnailUrl: String)