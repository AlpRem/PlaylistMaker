package com.practicum.playlistmaker.library.ui

import android.net.Uri
import com.practicum.playlistmaker.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.library.domain.model.Playlist
import java.io.File

class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

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
}