package com.practicum.playlistmaker.track.domain.impl

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.track.domain.api.TrackRepository
import com.practicum.playlistmaker.track.domain.api.TracksInteractor


class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

    private val handler = Handler(Looper.getMainLooper())

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        Thread {
            val result = repository.getTracks(expression)
            handler.post {
                consumer.consume(result)
            }
        }.start()

    }
}