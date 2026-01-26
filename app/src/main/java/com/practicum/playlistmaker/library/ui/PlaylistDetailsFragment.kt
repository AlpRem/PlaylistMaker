package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.util.dpToPx
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.domain.model.PlaylistAddState
import com.practicum.playlistmaker.library.domain.model.PlaylistDetailsState
import com.practicum.playlistmaker.library.presenter.PlaylistDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistDetailsFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistDetailsBinding
    private val viewModel: PlaylistDetailsViewModel by viewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
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

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        viewModel.loadPlaylist(requireArguments().getLong(PLAYLIST_ID))
    }


    private fun toBackArrowButton() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun render(state: PlaylistDetailsState) {
        when {
            state.isLoading -> showLoading()
            state.isEmpty -> showEmpty()
            state.playlist != null -> showPlaylist(state.playlist, state.totalDurationMillis)
        }
    }
    private fun showLoading() {
    }

    private fun showEmpty() {
    }

    private fun showPlaylist(playlist: Playlist, durationMillis: Long) {
        if (playlist.image.isBlank())
            binding.playlistImage.setImageResource(R.drawable.placeholder)
        else
            Glide.with(this)
                .load(File(playlist.image))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(binding.playlistImage)
        binding.name.text = playlist.name
        binding.description.text = playlist.description
        val minutes = durationMillis / 1000 / 60
        binding.time.text = this.resources.getQuantityString(
            R.plurals.time_count,
            minutes.toInt() ,
            minutes
        )

        binding.countTrack.text = this.resources.getQuantityString(
            R.plurals.tracks_count,
            playlist.countTracks,
            playlist.countTracks
        )
    }



    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"
        fun createArgs(id: Long): Bundle =
            bundleOf(PLAYLIST_ID to id)
    }
}