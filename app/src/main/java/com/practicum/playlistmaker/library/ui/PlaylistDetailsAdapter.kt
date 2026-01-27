package com.practicum.playlistmaker.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TrackViewHolder

class PlaylistDetailsAdapter(var tracks: List<Track>,
                             private val onTrackClick: (Track) -> Unit,
                             private val onTrackLongClick: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size;
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onTrackClick(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            onTrackLongClick(track)
            true
        }
    }

    fun updatePage(page: Page<Track>) {
        tracks = page.data.toList()
        notifyDataSetChanged()
    }
}