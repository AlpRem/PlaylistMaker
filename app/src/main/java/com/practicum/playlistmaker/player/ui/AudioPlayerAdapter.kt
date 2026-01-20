package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.library.domain.model.Playlist

class AudioPlayerAdapter(var playlists: List<Playlist>,
                         private val onAddPlaylistClick: (Playlist) -> Unit): RecyclerView.Adapter<AudioPlayerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AudioPlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_bs, parent, false)
        return AudioPlayerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AudioPlayerViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onAddPlaylistClick(playlists[position])
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updatePage(page: Page<Playlist>) {
        playlists = page.data.toList()
        notifyDataSetChanged()
    }
}