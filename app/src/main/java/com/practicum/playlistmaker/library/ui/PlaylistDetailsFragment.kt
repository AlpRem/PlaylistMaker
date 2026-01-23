package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.practicum.playlistmaker.library.presenter.PlaylistDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistDetailsBinding
    private val viewModel: PlaylistDetailsViewModel by viewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = requireArguments().getLong(PLAYLIST_ID)
        viewModel.loadPlaylist(playlistId)
    }


    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"
        fun createArgs(id: Long): Bundle =
            bundleOf(PLAYLIST_ID to id)
    }
}