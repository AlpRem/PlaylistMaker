package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.db.data.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao: BaseDao<PlaylistEntity> {
    @Query("SELECT * FROM playlist")
    fun list(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist WHERE id = :id")
    suspend fun findById(id: Long): PlaylistEntity?

    @Update
    suspend fun update(playlist: PlaylistEntity)
}