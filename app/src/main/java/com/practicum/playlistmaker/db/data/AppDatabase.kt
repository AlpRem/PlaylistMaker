package com.practicum.playlistmaker.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.db.data.dao.PlaylistDao
import com.practicum.playlistmaker.db.data.dao.TrackDao
import com.practicum.playlistmaker.db.data.dao.TrackInPlaylistDao
import com.practicum.playlistmaker.db.data.entity.PlaylistEntity
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.db.data.entity.TrackInPlaylistEntity

@Database(version = 1, entities = [
    TrackEntity::class,
    PlaylistEntity::class,
    TrackInPlaylistEntity::class], exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao
}