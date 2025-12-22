package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.TrackEntity

@Dao
interface TrackDao: BaseDao<TrackEntity>  {
    @Query("SELECT * FROM track")
    suspend fun list(): List<TrackEntity>

    @Query("SELECT * FROM track WHERE id = :id")
    suspend fun findById(id: String): TrackEntity?
}