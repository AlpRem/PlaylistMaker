package com.practicum.playlistmaker.library.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(t: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(t: List<T>)

    @Delete
    suspend fun delete(t: T)
}