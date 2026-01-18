package com.practicum.playlistmaker.player.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.common.util.dpToPx
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.domain.model.PlaylistState
import com.practicum.playlistmaker.library.ui.PlaylistAdapter
import com.practicum.playlistmaker.player.domain.model.AddTrackToPlaylistState
import com.practicum.playlistmaker.player.domain.model.AudioPlayerFragmentState
import com.practicum.playlistmaker.player.domain.model.AudioPlayerState
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.presenter.AudioPlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class AudioPlayerFragment: Fragment() {

    private lateinit var binding: FragmentAudioPlayerBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var audioPlayerAdapter: AudioPlayerAdapter

    private val audioPlayerViewModel: AudioPlayerViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toBackArrowButton()

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
            when (state.audioPlayerState.playerState) {
                is PlayerState.Playing -> binding.play.setImageResource(R.drawable.pause)
                else -> binding.play.setImageResource(R.drawable.play)
            }
            binding.timer.text = state.audioPlayerState.timerState
            binding.like.setImageResource(if (state.audioPlayerState.likeState) R.drawable.like_full else R.drawable.like)
            renderPlaylists(state.playlistState)
            renderAddTrackState(state.addTrackState)

        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    binding.overlay.visibility =
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            View.GONE
                        } else {
                            View.VISIBLE
                        }
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            }
        )

        initRecyclerView()

        binding.play.setOnClickListener { audioPlayerViewModel.playbackControl() }
        binding.like.setOnClickListener { audioPlayerViewModel.toggleLike() }
        binding.addToPlaylist.setOnClickListener {
            audioPlayerViewModel.getPlaylist()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.createPlaylistBtn.setOnClickListener {
            findNavController()
                .navigate(R.id.action_audioPlayerFragment_to_playlistAddFragment)
        }
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

    private fun toBackArrowButton() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initRecyclerView() {
        audioPlayerAdapter = AudioPlayerAdapter(emptyList()) {
            playlist ->
            audioPlayerViewModel.addPlaylist(playlist)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = audioPlayerAdapter
    }

    private fun renderAddTrackState(state: AddTrackToPlaylistState) {
        when {
            state.isAdded -> {
                showToast(getString(R.string.track_added), state.playlistName)
                audioPlayerViewModel.clearAddTrackState()
            }

            state.isExists -> {
                showToast(getString(R.string.track_is_exists),state.playlistName)
                audioPlayerViewModel.clearAddTrackState()
            }
        }
    }

    private fun renderPlaylists(state: PlaylistState) {
        when {
            state.isLoading -> {}
            state.isError -> showErrors()
            state.isEmpty -> showErrors()
            else -> showContent(state.page)
        }
    }

    private fun showErrors() {
        binding.recyclerView.visibility = View.GONE
    }

    private fun showContent(playlists: Page<Playlist>) {
        binding.recyclerView.visibility = View.VISIBLE
        audioPlayerAdapter.updatePage(playlists)
    }

    private fun showToast(message: String, playlistName: String) {
        Toast.makeText(requireContext(), "$message $playlistName",Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARGS_TRACK = "TRACK"
        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to Gson().toJson(track))
    }
}