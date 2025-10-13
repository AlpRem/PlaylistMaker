package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
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
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.common.ui.BaseActivity
import com.practicum.playlistmaker.common.util.dpToPx
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.player.domain.model.AudioPlayerState
import com.practicum.playlistmaker.player.presenter.AudioPlayerViewModel
import com.practicum.playlistmaker.player.presenter.LikeViewModel
import kotlin.getValue

class AudioPlayerActivity : BaseActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private val audioPlayerViewModel: AudioPlayerViewModel by viewModels()

    private val  likeViewModel: LikeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                is AudioPlayerState.Playing -> binding.play.setImageResource(R.drawable.pause)
                else -> binding.play.setImageResource(R.drawable.play)
            }
        }

        audioPlayerViewModel.observeTimer.observe(this) { binding.timer.text = it }

        likeViewModel.observeLike.observe(this) {isLike ->
            binding.like.setImageResource(
                if (isLike) R.drawable.like_full else R.drawable.like
            )
        }
        binding.play.setOnClickListener { audioPlayerViewModel.playbackControl() }
        binding.like.setOnClickListener { likeViewModel.toggleLike() }
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
                .into(binding.cover)
        binding.trackName.text = track.trackName
        binding.trackAuthor.text = track.artistName
        binding.timer.text = getString(R.string.null_timer)
        gerContentInLineTextViews(track.trackTime, binding.trackTimeTitle, binding.trackTimeValue)
        gerContentInLineTextViews(track.collectionName, binding.collectionNameTitle, binding.collectionNameValue)
        gerContentInLineTextViews(track.releaseDate, binding.releaseDateTitle, binding.releaseDateValue)
        gerContentInLineTextViews(track.primaryGenreName, binding.primaryGenreNameTitle, binding.primaryGenreNameValue)
        gerContentInLineTextViews(track.country, binding.countryTitle, binding.countryValue)
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