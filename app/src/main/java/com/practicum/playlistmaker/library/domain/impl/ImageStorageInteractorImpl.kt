package com.practicum.playlistmaker.library.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.library.domain.api.ImageStorageInteractor
import com.practicum.playlistmaker.library.domain.api.ImageStorageRepository

class ImageStorageInteractorImpl(private val imageStorageRepository: ImageStorageRepository): ImageStorageInteractor {
    override suspend fun saveImage(uri: Uri): String {
        return imageStorageRepository.saveImage(uri)
    }
}