package com.practicum.playlistmaker.track.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.track.model.Track
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.component.Page

class TrackAdapter(var tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size;
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    fun updatePage(page: Page<Track>) {
        tracks = page.data as List<Track>
        notifyDataSetChanged()
    }
}