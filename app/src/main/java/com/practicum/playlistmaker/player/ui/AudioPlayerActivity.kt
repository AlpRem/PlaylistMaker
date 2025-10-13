package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.track.domain.model.Track
import com.practicum.playlistmaker.common.ui.BaseActivity
import com.practicum.playlistmaker.common.util.dpToPx
import com.practicum.playlistmaker.player.domain.model.AudioPlayerState
import com.practicum.playlistmaker.player.presenter.AudioPlayerViewModel
import com.practicum.playlistmaker.player.presenter.LikeViewModel
import kotlin.getValue

class AudioPlayerActivity : BaseActivity() {
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

    private val audioPlayerViewModel: AudioPlayerViewModel by viewModels {
        AudioPlayerViewModel.getFactory()
    }

    private val  likeViewModel: LikeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        onInitElement()

        arrowBackButton(R.id.arrow_back)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_audio_player)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }

        val track =  Gson().fromJson(intent.getStringExtra("TRACK") , Track::class.java)
        onInitData(track)
        if (audioPlayerViewModel.track == null)
            audioPlayerViewModel.preparePlayer(track)

        audioPlayerViewModel.observeStateAudioPlayer.observe(this) { state ->
            when (state) {
                is AudioPlayerState.Playing -> playButton.setImageResource(R.drawable.pause)
                else -> playButton.setImageResource(R.drawable.play)
            }
        }

        audioPlayerViewModel.observeTimer.observe(this) { timer.text = it }

        likeViewModel.observeLike.observe(this) {isLike ->
            likeButton.setImageResource(
                if (isLike) R.drawable.like_full else R.drawable.like
            )
        }
        playButton.setOnClickListener { audioPlayerViewModel.playbackControl() }
        likeButton.setOnClickListener { likeViewModel.toggleLike() }
    }

    fun onInitElement() {
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
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        audioPlayerViewModel.onPause()
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
}