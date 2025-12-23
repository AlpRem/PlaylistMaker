package com.practicum.playlistmaker.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeParseException
import java.util.Locale

fun formatDuration(millis: Long?): String {
    return millis?.let {
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
    } ?: "00:00"
}

fun formatDuration(millis: String?): Long? {
    if (millis.isNullOrBlank()) return null
    val parts = millis.split(":")
    if (parts.size != 2) return null
    val minutes = parts[0].toLongOrNull() ?: return null
    val seconds = parts[1].toLongOrNull() ?: return null
    if (seconds !in 0..59) return null
    return (minutes * 60 + seconds) * 1_000
}

fun extractYear(dateString: String?): String {
    if (dateString.isNullOrEmpty()) {
        return ""
    }
    return try {
        Instant.parse(dateString).atZone(ZoneId.systemDefault()).year.toString()
    } catch (e: DateTimeParseException) {
        ""
    }
}