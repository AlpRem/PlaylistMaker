package com.practicum.playlistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.domain.model.Playlist
import java.io.File

class AudioPlayerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val imagePlaylist: ImageView = itemView.findViewById(R.id.playlist_image)
    private val namePlaylist: TextView = itemView.findViewById(R.id.name_playlist)
    private val countTrack: TextView = itemView.findViewById(R.id.count_track)

    fun bind(model: Playlist) {

        namePlaylist.text = model.name
        countTrack.text = itemView.resources.getQuantityString(
            R.plurals.tracks_count,
            model.countTracks,
            model.countTracks
        )
        if (model.image.isBlank())
            imagePlaylist.setImageResource(R.drawable.placeholder)
        else
            Glide.with(itemView)
                .load(File(model.image))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(imagePlaylist)

    }

//    private fun trackTransformValue(count: Int): String {
//        return when {
//            count % 10 == 1 && count % 100 != 11 -> "трек"
//            count % 10 in 2..4 && count % 100 !in 12..14 -> "трека"
//            else -> "треков"
//        }
//    }
}