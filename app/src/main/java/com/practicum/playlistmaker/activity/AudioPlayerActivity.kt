package com.practicum.playlistmaker.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.activity.base.BaseActivity
import com.practicum.playlistmaker.provider.AudioPlayerProvider
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.util.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity(
    private val audioPlayerProvider: AudioPlayerProvider = AudioPlayerProvider())  : BaseActivity() {

    private lateinit var handler: Handler
    private val timerRunnable = Runnable { setTimerValueRunnable() }
    private lateinit var cover: ImageView
    private lateinit var trackName: TextView
    private lateinit var trackAuthor: TextView
    private lateinit var timer: TextView
    private lateinit var trackTimeTitle: TextView
    private lateinit var trackTimeValue: TextView
    private lateinit var collectionNameTitle: TextView
    private lateinit var collectionNameValue: TextView
    private lateinit var releaseDateTitle: TextView
    private lateinit var releaseDateValue: TextView
    private lateinit var primaryGenreNameTitle: TextView
    private lateinit var primaryGenreNameValue: TextView
    private lateinit var countryTitle: TextView
    private lateinit var countryValue: TextView
    private lateinit var playButton: ImageView
    private lateinit var likeButton: ImageView
    private var audioPlayerState: AudioPlayerState = AudioPlayerState.Default
    private var isLike = false
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        val trackGson = intent.getStringExtra("TRACK") ?: ""
        val track =  Gson().fromJson(trackGson ,Track::class.java)
        super.onCreate(savedInstanceState)
        handler = Handler(Looper.getMainLooper())
        setContentView(R.layout.activity_audio_player)
        mediaPlayer = audioPlayerProvider.createMediaPlayer()
        cover = findViewById(R.id.cover)
        playButton = findViewById(R.id.play)
        likeButton = findViewById(R.id.like)
        trackName = findViewById(R.id.trackName)
        trackAuthor = findViewById(R.id.trackAuthor)
        timer = findViewById(R.id.timer)
        trackTimeTitle = findViewById(R.id.track_time_title)
        trackTimeValue = findViewById(R.id.track_time_value)
        collectionNameTitle = findViewById(R.id.collection_name_title)
        collectionNameValue = findViewById(R.id.collection_name_value)
        releaseDateTitle = findViewById(R.id.release_date_title)
        releaseDateValue = findViewById(R.id.release_date_value)
        primaryGenreNameTitle = findViewById(R.id.primary_genre_name_title)
        primaryGenreNameValue = findViewById(R.id.primary_genre_name_value)
        countryTitle = findViewById(R.id.country_title)
        countryValue = findViewById(R.id.country_value)

        onInitData(track)
        preparePlayer(track)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_audio_player)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }
        arrowBackButton(R.id.arrow_back)
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun onInitData(track: Track) {
        if (!track.artworkUrl100.isEmpty())
            Glide.with(this)
                .load(getHeightArtworkUrl(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(8.dpToPx())
                    )
                )
                .into(cover)
        trackName.text = track.trackName
        trackAuthor.text = track.artistName
        timer.text = getString(R.string.null_timer)
        gerContentInLineTextViews(track.trackTime, trackTimeTitle, trackTimeValue)
        gerContentInLineTextViews(track.collectionName, collectionNameTitle, collectionNameValue)
        gerContentInLineTextViews(track.releaseDate, releaseDateTitle, releaseDateValue)
        gerContentInLineTextViews(track.primaryGenreName, primaryGenreNameTitle, primaryGenreNameValue)
        gerContentInLineTextViews(track.country, countryTitle, countryValue)
        changeStatusLikeTrack(track)
    }

    fun getHeightArtworkUrl(artworkUrl100: String): String {
        return if (artworkUrl100.endsWith("100x100bb.jpg")) {
            artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
        } else {
            artworkUrl100
        }
    }

    private fun gerContentInLineTextViews(content: String, title: TextView, value: TextView) {
        if (content.isEmpty()) {
            title.visibility = View.GONE
            value.visibility = View.GONE
        } else {
            title.visibility = View.VISIBLE
            value.visibility = View.VISIBLE
            value.text = content
        }
    }

    private fun changeStatusLikeTrack(track: Track) {
        likeButton.setOnClickListener {
            isLike = !isLike
            if (isLike) {
                likeButton.setImageResource(R.drawable.like_full)
            } else {
                likeButton.setImageResource(R.drawable.like)
            }
        }
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            audioPlayerState = AudioPlayerState.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            audioPlayerState = AudioPlayerState.Prepared
            playButton.setImageResource(R.drawable.play)
            handler.removeCallbacks(timerRunnable)
            timer.text = getString(R.string.null_timer)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause)
        audioPlayerState = AudioPlayerState.Playing
        handler.postDelayed(timerRunnable, TIMER_UPDATE_DELAY)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play)
        audioPlayerState = AudioPlayerState.Paused
        handler.removeCallbacks(timerRunnable)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun playbackControl() {
        when(audioPlayerState) {
            AudioPlayerState.Playing -> {
                pausePlayer()
            }
            AudioPlayerState.Prepared, AudioPlayerState.Paused, AudioPlayerState.Default -> {
                startPlayer()
            }
        }
    }

    private fun setTimerValueRunnable() {
        timer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        if (audioPlayerState == AudioPlayerState.Playing) {
            handler.postDelayed(timerRunnable, TIMER_UPDATE_DELAY)
        }
    }

    sealed class AudioPlayerState {
        object Default : AudioPlayerState()
        object Prepared : AudioPlayerState()
        object Playing : AudioPlayerState()
        object Paused : AudioPlayerState()
    }

    companion object {
        const val TIMER_UPDATE_DELAY = 300L
    }
}