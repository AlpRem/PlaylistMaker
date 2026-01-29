package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.db.data.entity.TrackInPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackInPlaylistDao: BaseDao<TrackInPlaylistEntity>  {

    @Query("SELECT * FROM track_in_playlist")
    fun list(): Flow<List<TrackInPlaylistEntity>>

    @Query("SELECT * FROM track_in_playlist WHERE id IN (:ids)")
    suspend fun findByIds(ids: List<String>): List<TrackInPlaylistEntity>

    @Query("SELECT * FROM track_in_playlist WHERE id = :id")
    suspend fun findById(id: String): TrackInPlaylistEntity?
}