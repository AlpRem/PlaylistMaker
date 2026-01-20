package com.practicum.playlistmaker.library.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.practicum.playlistmaker.library.domain.api.ImageStorageRepository
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ImageStorageRepositoryImpl(private val context: Context) : ImageStorageRepository {
    override suspend fun saveImage(uri: Uri): String {
        val dir = File(context.getExternalFilesDir(null),"playlist_covers")
        if (!dir.exists())
            dir.mkdirs()
        val file = File(dir, "cover_${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(uri).use { input ->
            FileOutputStream(file).use { output ->
                BitmapFactory.decodeStream(input)
                    .compress(Bitmap.CompressFormat.JPEG, 30, output)
            }
        }
        return file.absolutePath
    }
}