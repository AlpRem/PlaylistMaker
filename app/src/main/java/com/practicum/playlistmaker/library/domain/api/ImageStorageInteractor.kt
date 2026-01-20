package com.practicum.playlistmaker.library.domain.api

import android.net.Uri

interface ImageStorageInteractor {
    suspend fun saveImage(uri: Uri): String
}