package com.example.emosense

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MusicAdapter(val context: Context, var videoList: List<Video>) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    //custom function, for onclick reference CodingStuff video
    var onItemClick : ((Video) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.music_view, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicAdapter.MusicViewHolder, position: Int) {
        holder.tvTitle.text = videoList[position].title

        Glide.with(holder.itemView.context)
            .load(videoList[position].thumbnailUrl)
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .into(holder.ivThumbnail)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(videoList[position])
        }
    }

    override fun getItemCount(): Int {
        //The ?: operator means "if journalList is not null, return its size, otherwise return 0".

        if (videoList.isEmpty())
            return 0

        return videoList.size
    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        var ivThumbnail = itemView.findViewById<ImageView>(R.id.ivThumbnail)
    }
}
