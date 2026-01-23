package com.practicum.playlistmaker.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track

class PlaylistAdapter(var playlists: List<Playlist>,
                      private val onPlaylistClick: (Playlist) -> Unit): RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onPlaylistClick(playlists[position])
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