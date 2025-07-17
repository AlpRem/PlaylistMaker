package com.practicum.playlistmaker.track.adapter

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.track.model.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val trackName: TextView
    private val artistName: TextView
    private val trackTime: TextView
    private val artworkUrl: ImageView

    init {
        trackName = itemView.findViewById(R.id.trackName)
        artistName = itemView.findViewById(R.id.artistName)
        trackTime = itemView.findViewById(R.id.trackTime)
        artworkUrl = itemView.findViewById(R.id.artworkUrl)
    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .override(45.dpToPx(), 45.dpToPx())
            .centerCrop()
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(2.dpToPx())
                )
            )
            .into(artworkUrl)
    }

    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}