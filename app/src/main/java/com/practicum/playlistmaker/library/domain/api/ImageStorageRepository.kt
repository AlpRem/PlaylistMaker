package com.practicum.playlistmaker.library.domain.api

import android.net.Uri

interface ImageStorageRepository {
    suspend fun saveImage(uri: Uri): String
}