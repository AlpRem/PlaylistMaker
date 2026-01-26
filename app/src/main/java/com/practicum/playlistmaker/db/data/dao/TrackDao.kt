package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao: BaseDao<TrackEntity>  {
    @Query("SELECT * FROM track WHERE isFavorite = 1")
    fun findByFavorite(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track WHERE isPlaylist = 1")
    fun findByPlaylist(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track WHERE id = :id")
    suspend fun findById(id: String): TrackEntity?

    @Query("SELECT id FROM track WHERE isFavorite = 1")
    fun findByFavoriteAllIds(): Flow<List<String>>

    @Query("SELECT * FROM track WHERE id = :id AND isFavorite = 1")
    suspend fun findByIdAndFavorite(id: String): TrackEntity?

    @Query("UPDATE track SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE track SET isPlaylist = :isPlaylist WHERE id = :id")
    suspend fun updatePlaylist(id: String, isPlaylist: Boolean)

    @Query("SELECT * FROM track WHERE id IN (:ids)")
    suspend fun findByIds(ids: List<String>): List<TrackEntity>
}