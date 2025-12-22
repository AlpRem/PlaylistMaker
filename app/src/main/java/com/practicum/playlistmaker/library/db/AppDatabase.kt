package com.practicum.playlistmaker.library.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.library.db.dao.TrackDao
import com.practicum.playlistmaker.library.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao

}