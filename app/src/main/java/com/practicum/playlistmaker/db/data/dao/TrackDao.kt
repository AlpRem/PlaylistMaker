package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao: BaseDao<TrackEntity>  {
    @Query("SELECT * FROM track")
    fun list(): Flow<List<TrackEntity>>

    @Query("SELECT id FROM track")
    fun listAllIds(): Flow<List<String>>

    @Query("SELECT * FROM track WHERE id = :id")
    suspend fun findById(id: String): TrackEntity?
}