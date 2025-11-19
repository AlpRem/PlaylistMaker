package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.util.dpToPx
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.player.domain.model.AudioPlayerState
import com.practicum.playlistmaker.player.presenter.AudioPlayerViewModel
import com.practicum.playlistmaker.player.presenter.LikeViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class AudioPlayerFragment: Fragment() {

    companion object {
        private const val ARGS_TRACK = "TRACK"
        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to Gson().toJson(track))
    }

    private lateinit var binding: FragmentAudioPlayerBinding

    private val audioPlayerViewModel: AudioPlayerViewModel by viewModel()
    private val  likeViewModel: LikeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrowBackButton()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top)
            insets
        }
        val track =  Gson().fromJson(requireArguments().getString(ARGS_TRACK), Track::class.java)
        onInitData(track)
        if (audioPlayerViewModel.track == null)
            audioPlayerViewModel.preparePlayer(track)

        audioPlayerViewModel.observeStateAudioPlayer.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AudioPlayerState.Playing -> binding.play.setImageResource(R.drawable.pause)
                else -> binding.play.setImageResource(R.drawable.play)
            }
        }

        audioPlayerViewModel.observeTimer.observe(viewLifecycleOwner) { binding.timer.text = it }

        likeViewModel.observeLike.observe(viewLifecycleOwner) {isLike ->
            binding.like.setImageResource(
                if (isLike) R.drawable.like_full else R.drawable.like
            )
        }
        binding.play.setOnClickListener { audioPlayerViewModel.playbackControl() }
        binding.like.setOnClickListener { likeViewModel.toggleLike() }
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

    private fun arrowBackButton() {
        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}